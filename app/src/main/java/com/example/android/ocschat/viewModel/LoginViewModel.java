package com.example.android.ocschat.viewModel;

import com.google.firebase.auth.FirebaseUser;

import io.reactivex.Maybe;

public interface LoginViewModel {

    Maybe<FirebaseUser> register(String email, String name, String password);
    Maybe<FirebaseUser> login(String email, String password);
    Boolean checkEmptyInputs(String... inputs);
    Boolean isValidEmail(String email);
    Boolean isValidName(String name);
    Boolean isValidPassword(String password);

}
