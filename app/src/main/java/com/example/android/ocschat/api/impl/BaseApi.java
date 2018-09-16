package com.example.android.ocschat.api.impl;

import com.example.android.ocschat.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Maybe;

public class BaseApi {

    public Maybe<DataSnapshot> getUser(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY).child(userId);
        return RxFirebaseDatabase.observeSingleValueEvent(userRef);
    }

}
