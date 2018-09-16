package com.example.android.ocschat.viewModel;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;


import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface LoginViewModel {

    Completable register(HashMap<String, Object> body);
    Maybe<FirebaseUser> login(HashMap<String, String> body);
    Boolean checkEmptyInputs(String... inputs);
    Single<Uri> uploadImage(Uri filePath);

}
