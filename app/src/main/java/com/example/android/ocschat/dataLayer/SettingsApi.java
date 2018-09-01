package com.example.android.ocschat.dataLayer;

import com.example.android.ocschat.model.User;
import com.google.firebase.database.DataSnapshot;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface SettingsApi {
    Maybe<DataSnapshot> getUser(String uid);
    Completable updateCurrentUser(User user);
}
