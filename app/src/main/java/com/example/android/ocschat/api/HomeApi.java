package com.example.android.ocschat.api;

import android.net.Uri;

import com.google.firebase.database.DataSnapshot;

import java.io.File;

import durdinapps.rxfirebase2.RxFirebaseChildEvent;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface HomeApi {

    Flowable<RxFirebaseChildEvent<DataSnapshot>> getCurrentUserFriends();
    Maybe<DataSnapshot> getUser(String friendId);
    Single<File> downloadImage(Uri downloadUrl, String userId);

}
