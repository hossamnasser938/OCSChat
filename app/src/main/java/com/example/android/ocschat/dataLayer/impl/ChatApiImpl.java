package com.example.android.ocschat.dataLayer.impl;

import com.example.android.ocschat.dataLayer.ChatApi;
import com.example.android.ocschat.util.Constants;
import com.example.android.ocschat.util.OCSChatThrowable;
import com.example.android.ocschat.util.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;

public class ChatApiImpl implements ChatApi {

    @Override
    public Flowable<RxFirebaseChildEvent<DataSnapshot>> getMessages(String friendId) throws OCSChatThrowable {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String messageKey = Utils.generateMessageKey(currentUserId, friendId);
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference().child(Constants.MESSAGES_KEY).child(messageKey);
        return RxFirebaseDatabase.observeChildEvent(messagesRef);
    }
}
