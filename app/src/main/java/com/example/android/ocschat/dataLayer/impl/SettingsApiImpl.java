package com.example.android.ocschat.dataLayer.impl;

import com.example.android.ocschat.dataLayer.SettingsApi;
import com.example.android.ocschat.util.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Maybe;

public class SettingsApiImpl implements SettingsApi {

    public Maybe<DataSnapshot> getUser(String uid){
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY).child(uid);
        return RxFirebaseDatabase.observeSingleValueEvent(userReference);
    }

}
