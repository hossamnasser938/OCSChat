package com.example.android.ocschat.viewModel.impl

import android.util.Log
import com.example.android.ocschat.dataLayer.AddFriendApi
import com.example.android.ocschat.model.User
import com.example.android.ocschat.viewModel.AddFriendViewModel
import io.reactivex.Flowable

class AddFriendViewModelImpl : AddFriendViewModel {

    private val api : AddFriendApi

    constructor(api: AddFriendApi) {
        this.api = api
    }

    override fun getAllUsers() : Flowable<User> {
        return api.allUsers.flatMap {
            val user = it.value.getValue(User::class.java)
            Flowable.just(user)
        }
    }
}