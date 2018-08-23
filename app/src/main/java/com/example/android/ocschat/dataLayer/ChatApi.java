package com.example.android.ocschat.dataLayer;

import com.example.android.ocschat.util.OCSChatThrowable;
import com.google.firebase.database.DataSnapshot;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import io.reactivex.Flowable;

public interface ChatApi {

    Flowable<RxFirebaseChildEvent<DataSnapshot>> getMessages(String friendId) throws OCSChatThrowable;

}
