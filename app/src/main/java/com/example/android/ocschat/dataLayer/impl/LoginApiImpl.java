package com.example.android.ocschat.dataLayer.impl;

import com.example.android.ocschat.dataLayer.LoginApi;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import durdinapps.rxfirebase2.RxFirebaseAuth;
import io.reactivex.Maybe;

public class LoginApiImpl implements LoginApi {

    @Override
    public Maybe<AuthResult> register(String email, String name, String password) {
        return RxFirebaseAuth.createUserWithEmailAndPassword(FirebaseAuth.getInstance(), email, password);
    }

    @Override
    public Maybe<AuthResult> login(String email, String password) {
        return RxFirebaseAuth.signInWithEmailAndPassword(FirebaseAuth.getInstance(), email, password);
    }
}
