package com.example.android.ocschat.viewModel;

import com.example.android.ocschat.model.User;

import io.reactivex.Flowable;

public interface HomeViewModel {

    Flowable<User> getCurrentUserFriends();

}
