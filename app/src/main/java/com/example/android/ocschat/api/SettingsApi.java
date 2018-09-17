package com.example.android.ocschat.api;

import android.net.Uri;

import com.example.android.ocschat.model.User;
import com.google.firebase.database.DataSnapshot;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface SettingsApi {

    Completable updateCurrentUser(User user);
    Single<Uri> uploadImage(Uri filePath);

}
