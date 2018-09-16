package com.example.android.ocschat.viewModel;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

import io.reactivex.Completable;
import io.reactivex.Maybe;

public interface LoginViewModel {

    Maybe<AuthResult> registerInAuth(HashMap<String, Object> body);
    Completable registerInRealtimeDatabase(HashMap<String, Object> body);
    Maybe<FirebaseUser> login(HashMap<String, String> body);
    Boolean checkEmptyInputs(String... inputs);

}
