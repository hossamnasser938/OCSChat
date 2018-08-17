package com.example.android.ocschat.viewModel;

import com.google.firebase.auth.AuthResult;

import io.reactivex.Maybe;

public interface LoginViewModel {

    Maybe<AuthResult> register(String email, String name, String password);
    Maybe<AuthResult> login(String email, String password);

}
