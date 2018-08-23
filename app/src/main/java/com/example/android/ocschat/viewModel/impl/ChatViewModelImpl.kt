package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.dataLayer.ChatApi
import com.example.android.ocschat.model.Message
import com.example.android.ocschat.model.User
import com.example.android.ocschat.viewModel.ChatViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ChatViewModelImpl : ChatViewModel {

    private var api : ChatApi

    constructor(api: ChatApi) {
        this.api = api
    }

    override fun getMessages(friendId: String?): Flowable<Message> {
        return api.getMessages(friendId).flatMap {
            val message = it.value.getValue(Message::class.java)
            Flowable.just(message)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun pushMessage(friendId: String?, message: Message?): Completable {
        return api.pushMessage(friendId, message)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getUser(userid: String?): Maybe<User> {
        return api.getUser(userid).flatMap {
            val user = it.getValue(User::class.java)
            Maybe.just(user)
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}