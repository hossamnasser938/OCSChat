package com.example.android.ocschat.dataLayer.impl;

import com.example.android.ocschat.dataLayer.AddFriendApi;
import com.example.android.ocschat.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;

public class AddFriendApiImpl implements AddFriendApi {

    @Override
    public Flowable<RxFirebaseChildEvent<DataSnapshot>> getAllUsers() {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY);
        return RxFirebaseDatabase.observeChildEvent(usersReference);
    }
}
