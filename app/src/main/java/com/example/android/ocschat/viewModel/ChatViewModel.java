package com.example.android.ocschat.viewModel;

import com.example.android.ocschat.model.Message;
import com.example.android.ocschat.model.User;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ChatViewModel {

    Flowable<Message> getMessages(String friendId);
    Completable pushMessage(String friendId, Message message);
    Single<User> getUser(String userid);

}
