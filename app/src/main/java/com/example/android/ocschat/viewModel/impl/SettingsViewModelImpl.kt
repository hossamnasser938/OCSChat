package com.example.android.ocschat.viewModel.impl

import android.net.Uri
import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.SettingsRepository
import com.example.android.ocschat.repository.impl.BaseRepository
import com.example.android.ocschat.viewModel.SettingsViewModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SettingsViewModelImpl(private val repository: SettingsRepository, private val baseRepository : BaseRepository) : BaseViewModel(baseRepository), SettingsViewModel {

    override fun updateCurrentUser(user: User?, filePath : Uri?): Completable {
        return repository.updateCurrentUser(user, filePath)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}