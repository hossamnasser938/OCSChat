package com.example.android.ocschat.repository.impl

import com.example.android.ocschat.api.ChatApi
import com.example.android.ocschat.localDatabase.Gate
import com.example.android.ocschat.model.Message
import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.ChatRepository
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class ChatRepositoryImpl : ChatRepository {

    private val gate : Gate
    private val api : ChatApi

    constructor(gate: Gate, api: ChatApi) {
        this.gate = gate
        this.api = api
    }

    override fun getMessages(friendId: String?): Flowable<Message> {
        return api.getMessages(friendId).flatMap {
            val message = it.value.getValue(Message::class.java)
            Flowable.just(message)
        }
    }

    override fun pushMessage(friendId: String?, message: Message?): Completable {
        return api.pushMessage(friendId, message)
    }

    override fun getUser(userid: String?): Single<User> {
        return gate.getUser(userid)
    }


}