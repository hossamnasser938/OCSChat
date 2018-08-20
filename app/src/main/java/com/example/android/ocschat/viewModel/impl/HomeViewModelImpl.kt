package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.dataLayer.HomeApi
import com.example.android.ocschat.model.Friend
import com.example.android.ocschat.model.User
import com.example.android.ocschat.util.OCSChatThrowable
import com.example.android.ocschat.viewModel.HomeViewMdel
import io.reactivex.Flowable

class HomeViewModelImpl : HomeViewMdel {

    private val api : HomeApi

    constructor(api: HomeApi) {
        this.api = api
    }


    override fun getCurrentUserFriends() : Flowable<List<User>> {
        return api.currentUserFriends
                .flatMap { DataSnapshot ->
                    if(DataSnapshot != null){
                        var friendsIdsList = ArrayList<String>()
                        var friendsList = ArrayList<User>()
                        for(dataSnapshot in DataSnapshot.children){
                            friendsIdsList.add((dataSnapshot.value as Friend).id)
                        }
                        api.getCurrentUserFriendsAsUsers(friendsIdsList)
                                .subscribe({ for(dataSnapshot in it){
                                    friendsList.add(dataSnapshot.value as User)
                                } } , { Flowable.error<OCSChatThrowable>(OCSChatThrowable("")) })
                        Flowable.just(friendsList)
                    }
                    else{
                        Flowable.error(OCSChatThrowable(""))
                    }
                }
    }
}