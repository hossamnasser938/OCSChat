package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.model.Message
import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.ChatRepository
import com.example.android.ocschat.viewModel.ChatViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ChatViewModelImpl : ChatViewModel {

    private var repository : ChatRepository

    constructor(repository : ChatRepository) {
        this.repository = repository
    }

    override fun getMessages(friendId: String?): Flowable<Message> {
        return repository.getMessages(friendId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun pushMessage(friendId: String?, message: Message?): Completable {
        return repository.pushMessage(friendId, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUser(userid: String?): Single<User> {
        return repository.getUser(userid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}