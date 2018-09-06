package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.api.SettingsApi
import com.example.android.ocschat.model.User
import com.example.android.ocschat.viewModel.SettingsViewModel
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

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
                }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun updateCurrentUser(user: User?): Completable {
        return api.updateCurrentUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}