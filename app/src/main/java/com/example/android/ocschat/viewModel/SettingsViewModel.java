package com.example.android.ocschat.viewModel;

import com.example.android.ocschat.model.User;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface SettingsViewModel {

    Single<User> getUser(String uid);
    Completable updateCurrentUser(User user);

}
