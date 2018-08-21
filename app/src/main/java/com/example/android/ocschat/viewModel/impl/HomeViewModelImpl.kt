package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.dataLayer.HomeApi
import com.example.android.ocschat.model.Friend
import com.example.android.ocschat.model.User
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.util.OCSChatThrowable
import com.example.android.ocschat.viewModel.HomeViewModel
import io.reactivex.Flowable

class HomeViewModelImpl : HomeViewModel {

    private val api : HomeApi

    constructor(api: HomeApi) {
        this.api = api
    }


    override fun getCurrentUserFriends() : Flowable<List<User>> {
        return api.currentUserFriends
                .flatMap { DataSnapshot ->
                    if(DataSnapshot != null){
                        //DataSnapshot here holds ids of current user friends
                        val friendsIdsList = ArrayList<String>()
                        val friendsList = ArrayList<User>()
                        for(dataSnapshot in DataSnapshot.children){
                            friendsIdsList.add((dataSnapshot.getValue(Friend::class.java)).id)
                        }
                        api.getCurrentUserFriendsAsUsers(friendsIdsList)
                                .subscribe({ for(dataSnapshot in it){
                                    //DataSnapshot here holds friends of current user as User objects
                                    friendsList.add(dataSnapshot.getValue(User::class.java))
                                } } , { Flowable.error<OCSChatThrowable>(OCSChatThrowable(it.message)) })
                        Flowable.just(friendsList)
                    }
                    else{
                        Flowable.error(OCSChatThrowable(Constants.FAILED_LOADING_FRIENDS))
                    }
                }
    }
}