package com.example.android.ocschat.dataLayer;

import com.google.firebase.database.DataSnapshot;

import io.reactivex.Maybe;

public interface SettingsApi {
    Maybe<DataSnapshot> getUser(String uid);
}
