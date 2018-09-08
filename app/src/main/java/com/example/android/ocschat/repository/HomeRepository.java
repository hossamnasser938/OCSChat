package com.example.android.ocschat.repository;

import com.example.android.ocschat.model.User;

import io.reactivex.Flowable;

public interface HomeRepository {

    Flowable<User> getCurrentUserFriends();

}
