package com.example.android.ocschat.dataLayer;

import com.google.firebase.database.DataSnapshot;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import io.reactivex.Flowable;

public interface AddFriendApi {

    Flowable<RxFirebaseChildEvent<DataSnapshot>> getAllUsers();

}
