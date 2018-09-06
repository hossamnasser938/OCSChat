package com.example.android.ocschat.viewModel.impl

import android.util.Log
import com.example.android.ocschat.api.HomeApi
import com.example.android.ocschat.model.Friend
import com.example.android.ocschat.model.User
import com.example.android.ocschat.viewModel.HomeViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModelImpl : HomeViewModel {

    private val api : HomeApi

    constructor(api: HomeApi) {
        this.api = api
    }


    override fun getCurrentUserFriends() : Flowable<ArrayList<User>> {
        return api.currentUserFriends
                .flatMap {
                        //it here holds ids of current user friends
                        val friendsIdsList = ArrayList<String>()
                        val friendsList = ArrayList<User>()
                        for(dataSnapshot in it.children){
                            val friend = dataSnapshot.getValue(Friend::class.java)
                            Log.d("HomeViewModelImpl", friend.id)
                            friendsIdsList.add(friend.id)
                        }
                        api.getCurrentUserFriendsAsUsers(friendsIdsList)
                                .flatMapPublisher { for(dataSnapshot in it){
                                    //it here holds friends of current user as User objects
                                    val friendUser = dataSnapshot.getValue(User::class.java)
                                    Log.d("HomeViewModelImpl", friendUser.name)
                                    friendsList.add(friendUser) }

                                    Flowable.just(friendsList)
                                    }
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}