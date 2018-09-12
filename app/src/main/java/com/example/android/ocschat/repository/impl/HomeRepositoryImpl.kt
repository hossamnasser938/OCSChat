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

        return gate.getUser(currentUserId).flatMapPublisher { currentUser ->
            api.currentUserFriends.flatMap {
                val friend = it.value.getValue(Friend::class.java)
                api.getUser(friend.id).flatMapPublisher {
                    val user = it.getValue(User::class.java)
                    gate.isFriend(user.id).flatMapPublisher {
                        friendsFetchedCounter++
                        if(!it) {
                            Log.d(TAG, "got user : " + user.firstName + " and inserted")
                            if(friendsFetchedCounter == currentUser.friendsCount){
                                Log.d(TAG, "reached last friend")
                                gate.addFriend(user)
                                        .andThen(Flowable.create( {
                                            it.onNext(user)
                                            it.onComplete()
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