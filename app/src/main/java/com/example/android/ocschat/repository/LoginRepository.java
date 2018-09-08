package com.example.android.ocschat.repository;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface LoginRepository {

    Completable register(HashMap<String, Object> body);
    Maybe<FirebaseUser> login(HashMap<String, String> body);

}
