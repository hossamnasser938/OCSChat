package com.example.android.ocschat.viewModel;

import android.net.Uri;

import com.example.android.ocschat.model.User;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface AddFriendViewModel {

    Flowable<User> getSuggestedUsers();
    Single<Boolean> isFriend(String uid);
    Completable addFriend(User user);
    Single<File> downloadImage(Uri downloadUrl, String useId);

}
