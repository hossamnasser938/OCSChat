package com.example.android.ocschat.repository;

import com.example.android.ocschat.model.Message;
import com.example.android.ocschat.model.User;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ChatRepository {

    Flowable<Message> getMessages(String friendId);
    Completable pushMessage(String friendId, Message message);
    Single<User> getUser(String userid);

}
