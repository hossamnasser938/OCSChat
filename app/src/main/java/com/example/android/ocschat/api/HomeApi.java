package com.example.android.ocschat.api;


import com.google.firebase.database.DataSnapshot;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;


public interface HomeApi {

    Flowable<DataSnapshot> getCurrentUserFriends();
    Single<List<DataSnapshot>> getCurrentUserFriendsAsUsers(List<String> friendsIdsList);

}
