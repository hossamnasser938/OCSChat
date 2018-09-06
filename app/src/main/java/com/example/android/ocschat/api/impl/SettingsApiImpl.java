package com.example.android.ocschat.api.impl;

import com.example.android.ocschat.api.SettingsApi;
import com.example.android.ocschat.model.User;
import com.example.android.ocschat.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Maybe;
import io.reactivex.functions.Function;

public class SettingsApiImpl implements SettingsApi {

    public Maybe<DataSnapshot> getUser(String uid){
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY).child(uid);
        return RxFirebaseDatabase.observeSingleValueEvent(userReference);
    }

    @Override
    public Completable updateCurrentUser(User user) {
        String currentUserId = user.getId();
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY);
        Map<String, Object> map = new HashMap<>();
        map.put(currentUserId, user);
        return RxFirebaseDatabase.updateChildren(usersRef, map);
    }
}
