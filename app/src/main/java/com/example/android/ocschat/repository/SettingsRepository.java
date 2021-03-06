package com.example.android.ocschat.repository;

import android.net.Uri;

import com.example.android.ocschat.model.User;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface SettingsRepository {

    Single<User> getUser(String uid);
    Completable updateCurrentUser(User user, Uri filePath);

}
