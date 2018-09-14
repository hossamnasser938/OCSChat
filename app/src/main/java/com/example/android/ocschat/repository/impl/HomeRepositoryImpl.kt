package com.example.android.ocschat.repository.impl

import android.util.Log
import com.example.android.ocschat.api.HomeApi
import com.example.android.ocschat.localDatabase.Gate
import com.example.android.ocschat.model.Friend
import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.HomeRepository
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class HomeRepositoryImpl : HomeRepository {

    private val TAG = "HomeRepository"

    private var friendsFetchedCounter = 0

    private val gate : Gate
    private val api : HomeApi

    constructor(gate: Gate, api : HomeApi) {
        this.gate = gate
        this.api = api
    }

    override fun getCurrentUserFriends(): Flowable<User> {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        return gate.getUserFriends(currentUserId)
    }

    override fun getJustLoggedUserFriends(): Flowable<User> {
        //get current user to find his friends count
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        return api.getUser(currentUserId).flatMapPublisher { datasnapshot ->
            val currentUser = datasnapshot.getValue(User::class.java)
            Log.d(TAG, "Current user " + currentUser.firstName + " friends count = " + currentUser.friendsCount)
            //get current user friends from api
            api.currentUserFriends.flatMap {
                friendsFetchedCounter++
                Log.d(TAG, "friendsFetchedCounter = " + friendsFetchedCounter)
                val friend = it.value.getValue(Friend::class.java)
                api.getUser(friend.id).flatMapPublisher {
                    val user = it.getValue(User::class.java)
                    gate.isFriend(user.id).flatMapPublisher {
                        //insert fetched users in local database if not inserted yet
                        if(!it) {
                            Log.d(TAG, "got user : " + user.firstName + " and inserted")
                            //if it is the last element just and onComplete
                            if(friendsFetchedCounter == currentUser.friendsCount){
                                Log.d(TAG, "reached last friend")
                                gate.addFriend(user)
                                        .andThen(Flowable.create( {
                                            it.onNext(user)
                                            it.onComplete()
                                            Log.d(TAG, "Sent on complete from getJustLoggedUserFriends")
                                        }, BackpressureStrategy.BUFFER))
                            }
                            else{
                                Log.d(TAG, "did not reach last friend")
                                gate.addFriend(user).andThen(Flowable.just(user))
                            }

                        }
                        else{
                            Log.d(TAG, "got user : " + user.firstName + " but not inserted")
                            if(friendsFetchedCounter == currentUser.friendsCount){
                                Log.d(TAG, "reached last friend")
                                Flowable.create( {
                                    it.onNext(user)
                                    it.onComplete()
                                }, BackpressureStrategy.BUFFER)
                            }
                            else{
                                Log.d(TAG, "did not reach last friend")
                                Flowable.just(user)
                            }

                        }
                    }
                }
            }
        }
        }



    override fun clearDatabase() {
        gate.clearDatabase()
    }
}