package com.example.android.ocschat.localDatabase;

import com.example.android.ocschat.model.User;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface Gate {

    Single<User> getUser(String userId);
    Flowable<User> getUserFriends(final String userId);
    Single<Boolean> isFriend(String friendId);
    Completable insertUser(User user);
    Completable addFriend(User user);
    Completable updateUser(User user);
    void clearDatabase();

}
