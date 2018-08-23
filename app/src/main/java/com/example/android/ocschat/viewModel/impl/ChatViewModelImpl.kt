package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.dataLayer.ChatApi
import com.example.android.ocschat.model.Message
import com.example.android.ocschat.viewModel.ChatViewModel
import io.reactivex.Flowable

class ChatViewModelImpl : ChatViewModel {

    private var api : ChatApi

    constructor(api: ChatApi) {
        this.api = api
    }

    override fun getMessages(friendId: String?): Flowable<Message> {
        return api.getMessages(friendId).flatMap {
            val message = it.value.getValue(Message::class.java)
            Flowable.just(message)
        }
    }
}