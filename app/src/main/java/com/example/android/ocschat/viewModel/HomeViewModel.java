package com.example.android.ocschat.viewModel;

import com.example.android.ocschat.model.User;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;

public interface HomeViewModel {

    Flowable<ArrayList<User>> getCurrentUserFriends();

}
