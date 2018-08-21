package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.dataLayer.AddFriendApi
import com.example.android.ocschat.model.User
import com.example.android.ocschat.viewModel.AddFriendViewModel
import io.reactivex.Flowable

class AddFriendViewModelImpl : AddFriendViewModel {

    private val api : AddFriendApi

    constructor(api: AddFriendApi) {
        this.api = api
    }

    override fun getAllUsers() : Flowable<List<User>> {
        return api.allUsers.flatMap {
            val usersList = ArrayList<User>()
            for (dsS in it.value.children) {
                val user = dsS.getValue(User::class.java)
                usersList.add(user)
            }
            Flowable.just(usersList)
        }
    }
}