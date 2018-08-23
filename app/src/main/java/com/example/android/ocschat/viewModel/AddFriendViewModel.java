package com.example.android.ocschat.viewModel;

import com.example.android.ocschat.model.Friend;
import com.example.android.ocschat.model.User;

import io.reactivex.Completable;
import io.reactivex.Flowable;


public interface AddFriendViewModel {

    Flowable<User> getAllUsers();
    Flowable<Boolean> isFriend(String uid);
    Completable addFriend(Friend friend);

}
