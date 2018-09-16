package com.example.android.ocschat.repository.impl

import android.content.Context
import android.util.Log
import com.example.android.ocschat.api.HomeApi
import com.example.android.ocschat.localDatabase.Gate
import com.example.android.ocschat.model.Friend
import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.HomeRepository
import com.example.android.ocschat.util.Utils
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable

class HomeRepositoryImpl(private val gate: Gate, private val api: HomeApi, private val context: Context) : HomeRepository {

    private val TAG = "HomeRepository"

    private var friendsFetchedCounter = 0

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
            //check if user has friends to download or not
            if(currentUser.friendsCount == 0){
                //user has no friends
                Flowable.create( {
                    it.onComplete()
                }, BackpressureStrategy.BUFFER)
            }
            else{
                //user has friends so get his friends from api
                api.currentUserFriends.flatMap {
                    val friend = it.value.getValue(Friend::class.java)
                    api.getUser(friend.id).flatMapPublisher {
                        val user = it.getValue(User::class.java)
                        gate.isFriend(user.id).flatMapPublisher {
                            //insert fetched users in local database if not inserted yet
                            if(!it) {
                                Log.d(TAG, "got user : " + user.firstName + " and inserted")

                                friendsFetchedCounter++
                                Log.d(TAG, "friendsFetchedCounter = " + friendsFetchedCounter)

                                //if it is the last element clear download flag
                                if(friendsFetchedCounter == currentUser.friendsCount){
                                    Log.d(TAG, "reached last friend")
                                    Utils.clearDownloadFlag(context)
                                    Log.d(TAG, "Download flag = " + Utils.isDownloadFlag(context))
                                }
                                else{
                                    Log.d(TAG, "did not reach last friend")
                                }
                                gate.addFriend(user).andThen(Flowable.just(user))

                            }
                            else{
                                Log.d(TAG, "got user : " + user.firstName + " but not inserted")
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