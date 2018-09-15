package com.example.android.ocschat.api.impl;

import android.util.Log;

import com.example.android.ocschat.api.AddFriendApi;
import com.example.android.ocschat.model.Friend;
import com.example.android.ocschat.model.User;
import com.example.android.ocschat.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.functions.Function;

class AddFriendApiImpl implements AddFriendApi {

    private final String TAG = "AddFriendIapiImpl";

    @Override
    public Flowable<RxFirebaseChildEvent<DataSnapshot>> getAllUsers() {
        DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY);
        return RxFirebaseDatabase.observeChildEvent(usersReference);
    }

    private Maybe<DataSnapshot> getUser(String uid){
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY).child(uid);
        return RxFirebaseDatabase.observeSingleValueEvent(userReference);
    }


    @Override
    public Completable addFriend(final Friend friend){
        //Add a friend to the current user
        Log.d(TAG, "add friend");

        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY);
        return getUser(currentUserId).flatMapCompletable(new Function<DataSnapshot, CompletableSource>() {
            @Override
            public CompletableSource apply(DataSnapshot dataSnapshot) throws Exception {
                User u = dataSnapshot.getValue(User.class);
                u.addFriend(friend);
                Map<String, Object> map = new HashMap<>();
                map.put(currentUserId, u);
                return RxFirebaseDatabase.updateChildren(usersRef, map)
                        .concatWith(confirmAddFriend(friend));
            }
        });
    }

    private Completable confirmAddFriend(final Friend friend) {
        //Add the current user as a friend to friend
        Log.d(TAG, "confirm add friend");

        final String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY);
        return getUser(friend.getId()).flatMapCompletable(new Function<DataSnapshot, CompletableSource>() {
            @Override
            public CompletableSource apply(DataSnapshot dataSnapshot) throws Exception {
                User u = dataSnapshot.getValue(User.class);
                u.addFriend(new Friend(currentUserId));
                Map<String, Object> map = new HashMap<>();
                map.put(u.getId(), u);
                return RxFirebaseDatabase.updateChildren(usersRef, map);
            }
        });
    }
}
