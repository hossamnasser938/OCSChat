package com.example.android.ocschat.api;

import com.example.android.ocschat.model.User;
import com.google.firebase.database.DataSnapshot;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface SettingsApi {

    Completable updateCurrentUser(User user);

}
