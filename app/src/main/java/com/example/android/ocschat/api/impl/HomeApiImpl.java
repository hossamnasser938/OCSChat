package com.example.android.ocschat.api.impl;

import com.example.android.ocschat.api.HomeApi;
import com.example.android.ocschat.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public class HomeApiImpl implements HomeApi {

    private static final String TAG = "HomeApiImpl";

    @Override
    public Flowable<RxFirebaseChildEvent<DataSnapshot>> getCurrentUserFriends() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference currentUserFriendsReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY).child(currentUserId).child(Constants.FRIENDS_KEY);

        return RxFirebaseDatabase.observeChildEvent(currentUserFriendsReference);
    }

    @Override
    public Maybe<DataSnapshot> getUser(String uid) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY).child(uid);

        return RxFirebaseDatabase.observeSingleValueEvent(userReference);
    }

}