package com.example.android.ocschat.dataLayer;

import com.google.firebase.auth.AuthResult;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface LoginApi {

    Maybe<AuthResult> registerInFirebaseAuth(String email, String password);
    Completable registerInFirebaseDatabase(String id, String name);
    Maybe<AuthResult> login(String email, String password);

}
