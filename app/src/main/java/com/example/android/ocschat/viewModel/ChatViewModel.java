package com.example.android.ocschat.viewModel;

import com.example.android.ocschat.model.Message;

import io.reactivex.Flowable;

public interface ChatViewModel {

    Flowable<Message> getMessages(String friendId);

}
