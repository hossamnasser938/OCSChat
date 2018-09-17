package com.example.android.ocschat.viewModel.impl

import android.net.Uri
import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.impl.BaseRepository
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class BaseViewModel(private val repository: BaseRepository) {

    fun getUser(uid: String?): Single<User> {
        return repository.getUser(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun downloadImage(downloadUrl : Uri, userId : String) : Single<Uri>{
        return repository.downloadImage(downloadUrl, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}