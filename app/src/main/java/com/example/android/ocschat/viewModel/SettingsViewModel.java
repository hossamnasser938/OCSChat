package com.example.android.ocschat.viewModel;

import com.example.android.ocschat.model.User;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface SettingsViewModel {
    Maybe<User> getUser(String uid);
    Completable updateCurrentUser(User user);
}
