package com.example.android.ocschat.dataLayer;

import com.google.firebase.auth.AuthResult;

import io.reactivex.Maybe;

public interface LoginApi {

    Maybe<AuthResult> register(String email, String name, String password);
    Maybe<AuthResult> login(String email, String password);

}
