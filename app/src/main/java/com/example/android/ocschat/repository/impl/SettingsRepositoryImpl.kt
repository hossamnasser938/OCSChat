package com.example.android.ocschat.repository.impl

import com.example.android.ocschat.api.SettingsApi
import com.example.android.ocschat.localDatabase.Gate
import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.SettingsRepository
import io.reactivex.Completable
import io.reactivex.Single

class SettingsRepositoryImpl : SettingsRepository {

    private val gate : Gate
    private val api : SettingsApi

    constructor(gate: Gate, api: SettingsApi) {
        this.gate = gate
        this.api = api
    }

    override fun getUser(uid: String?): Single<User> {
        return gate.getUser(uid)
    }

    override fun updateCurrentUser(user: User?): Completable {
        return gate.updateUser(user)
                .concatWith(api.updateCurrentUser(user))
    }


}