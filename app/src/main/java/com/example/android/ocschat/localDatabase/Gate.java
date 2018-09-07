package com.example.android.ocschat.localDatabase;

import com.example.android.ocschat.model.User;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface Gate {

    public Single<User> getUser(String userId);
    public Flowable<User> getUserFriends(final String userId);
    public Single<Boolean> isFriend(String userID, String friendId);
    public Completable insertUser(User user);
    public Completable updateUser(User user);

}
