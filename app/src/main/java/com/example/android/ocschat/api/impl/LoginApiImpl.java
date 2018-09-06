package com.example.android.ocschat.api.impl;


import com.example.android.ocschat.api.LoginApi;
import com.example.android.ocschat.model.User;
import com.example.android.ocschat.util.Constants;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import durdinapps.rxfirebase2.RxFirebaseAuth;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.Maybe;

public class LoginApiImpl implements LoginApi {

    @Override
    public Maybe<AuthResult> registerInFirebaseAuth(String email, String password) {
        return RxFirebaseAuth.createUserWithEmailAndPassword(FirebaseAuth.getInstance(), email, password);
    }

    @Override
    public Completable registerInFirebaseDatabase(User user){
        return RxFirebaseDatabase.setValue(FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY).child(user.getId()), user);
    }

    @Override
    public Maybe<AuthResult> login(String email, String password) {
        return RxFirebaseAuth.signInWithEmailAndPassword(FirebaseAuth.getInstance(), email, password);
    }
}
