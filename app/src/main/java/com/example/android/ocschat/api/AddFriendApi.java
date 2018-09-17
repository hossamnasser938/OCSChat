package com.example.android.ocschat.api;

import android.net.Uri;

import com.example.android.ocschat.model.Friend;
import com.google.firebase.database.DataSnapshot;

import java.io.File;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;


public interface AddFriendApi {

    Flowable<RxFirebaseChildEvent<DataSnapshot>> getAllUsers();
    Completable addFriend(Friend friend);
    Single<File> downloadImage(final Uri downloadUrl, final String userId);

}
