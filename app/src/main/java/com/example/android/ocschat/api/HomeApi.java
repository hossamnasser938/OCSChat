package com.example.android.ocschat.api;

import com.google.firebase.database.DataSnapshot;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import io.reactivex.Flowable;
import io.reactivex.Maybe;

public interface HomeApi {

    Flowable<RxFirebaseChildEvent<DataSnapshot>> getCurrentUserFriends();
    Maybe<DataSnapshot> getUser(String friendId);

}
