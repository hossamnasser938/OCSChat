package com.example.android.ocschat.api;

import android.net.Uri;

import com.example.android.ocschat.model.User;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;

import java.io.File;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

public interface LoginApi {

    Maybe<AuthResult> registerInFirebaseAuth(String email, String password);
    Completable registerInFirebaseDatabase(User user);
    Maybe<AuthResult> login(String email, String password);
    Maybe<DataSnapshot> getUser(String uid);
    Single<Uri> uploadImage(Uri filePath);
    Single<File> downloadImage(Uri downloadUrl, String userId);

}
