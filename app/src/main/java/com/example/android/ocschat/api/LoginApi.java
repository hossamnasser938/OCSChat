package com.example.android.ocschat.api;

import com.example.android.ocschat.model.User;
import com.google.firebase.auth.AuthResult;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface LoginApi {

    Maybe<AuthResult> registerInFirebaseAuth(String email, String password);
    Completable registerInFirebaseDatabase(User user);
    Maybe<AuthResult> login(String email, String password);

}
