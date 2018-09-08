package com.example.android.ocschat.viewModel;

import com.example.android.ocschat.model.Friend;
import com.example.android.ocschat.model.User;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;


public interface AddFriendViewModel {

    Flowable<User> getSuggestedUsers();
    Single<Boolean> isFriend(String uid);
    Completable addFriend(User user);

}
