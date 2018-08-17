package com.example.android.ocschat.viewModel;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import io.reactivex.Maybe;

public interface LoginViewModel {

    Maybe<FirebaseUser> register(HashMap<String, String> body);
    Maybe<FirebaseUser> login(HashMap<String, String> body);
    Boolean checkEmptyInputs(String... inputs);
    Boolean isValidEmail(String email);
    Boolean isValidName(String name);
    Boolean isValidPassword(String password);

}
