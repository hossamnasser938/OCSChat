package com.example.android.ocschat.api;

import com.example.android.ocschat.model.Friend;
import com.google.firebase.database.DataSnapshot;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import io.reactivex.Completable;
import io.reactivex.Flowable;


public interface AddFriendApi {

    Flowable<RxFirebaseChildEvent<DataSnapshot>> getAllUsers();
    Completable addFriend(Friend friend);

}
