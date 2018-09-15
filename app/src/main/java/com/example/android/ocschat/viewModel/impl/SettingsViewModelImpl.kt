package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.SettingsRepository
import com.example.android.ocschat.viewModel.SettingsViewModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SettingsViewModelImpl(private val repository: SettingsRepository) : SettingsViewModel {

    override fun getUser(uid: String?): Single<User> {
        return repository.getUser(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun updateCurrentUser(user: User?): Completable {
        return repository.updateCurrentUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}