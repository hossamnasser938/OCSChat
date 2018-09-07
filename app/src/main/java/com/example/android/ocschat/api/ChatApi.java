package com.example.android.ocschat.api;

import com.example.android.ocschat.model.Message;
import com.example.android.ocschat.util.OCSChatThrowable;
import com.google.firebase.database.DataSnapshot;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface ChatApi {

    Flowable<RxFirebaseChildEvent<DataSnapshot>> getMessages(String friendId) throws OCSChatThrowable;
    Completable pushMessage(String friendId, Message message) throws OCSChatThrowable;
    Maybe<DataSnapshot> getUser(String userId);  //TODO: remove

}
