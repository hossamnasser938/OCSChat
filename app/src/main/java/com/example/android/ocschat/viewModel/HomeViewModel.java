package com.example.android.ocschat.viewModel;

import com.example.android.ocschat.model.User;
import com.example.android.ocschat.model.UserState;

import java.util.List;

import io.reactivex.Flowable;

public interface HomeViewModel {

    Flowable<User> getCurrentUserFriends(UserState userState);
    void clearDatabase();

}
