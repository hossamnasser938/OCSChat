package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.dataLayer.SettingsApi
import com.example.android.ocschat.model.User
import com.example.android.ocschat.viewModel.SettingsViewModel
import io.reactivex.Maybe

class SettingsViewModelImpl : SettingsViewModel {

    private val api : SettingsApi

    constructor(api: SettingsApi) {
        this.api = api
    }

    override fun getUser(uid: String?): Maybe<User> {
        return api.getUser(uid)
                .flatMap {
                    val user = it.getValue(User::class.java)
                    Maybe.just(user)
                }
    }
}