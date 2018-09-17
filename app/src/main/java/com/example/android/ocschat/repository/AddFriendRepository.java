package com.example.android.ocschat.repository;

import android.net.Uri;

import com.example.android.ocschat.model.User;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface AddFriendRepository {

    Flowable<User> getCurrentUserFriends();
    Flowable<User> getCurrentUserNonFriends();
    Completable addFriend(User user);
    Single<Boolean> isFriend(String friendId);
    Single<Uri> downloadImage(Uri downloadUrl, String userId);

}
