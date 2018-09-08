package com.example.android.ocschat.repository;

import com.example.android.ocschat.model.Friend;
import com.example.android.ocschat.model.User;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface AddFriendRepository {

    Flowable<User> getCurrentUserFriends();
    Flowable<User> getCurrentUserNonFriends();
    Completable addFriend(Friend friend);
    Single<Boolean> isFriend(String friendId);

}
