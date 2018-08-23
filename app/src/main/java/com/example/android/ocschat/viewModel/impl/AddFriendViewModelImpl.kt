package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.dataLayer.AddFriendApi
import com.example.android.ocschat.model.Friend
import com.example.android.ocschat.model.User
import com.example.android.ocschat.viewModel.AddFriendViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddFriendViewModelImpl : AddFriendViewModel {

    private val api: AddFriendApi

    constructor(api: AddFriendApi) {
        this.api = api
    }

    override fun getAllUsers(): Flowable<User> {
        return api.allUsers.flatMap {
            val user = it.value.getValue(User::class.java)
            Flowable.just(user)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun isFriend(uid: String?): Flowable<Boolean> {
        return api.currentUserFriends
                .flatMap {
                    var isFriend = false
                    for (friend in it.children) {
                        val user = friend.getValue(Friend::class.java)
                        if (user.id.equals(uid, false))
                            isFriend = true
                    }
                    Flowable.just(isFriend)
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun addFriend(friend: Friend?): Completable {
        return api.addFriend(friend)
                .concatWith(api.confirmAddFriend(friend))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}