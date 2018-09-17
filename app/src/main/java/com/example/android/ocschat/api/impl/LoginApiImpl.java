package com.example.android.ocschat.api.impl;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.ocschat.R;
import com.example.android.ocschat.api.LoginApi;
import com.example.android.ocschat.model.User;
import com.example.android.ocschat.util.Constants;
import com.example.android.ocschat.util.OCSChatThrowable;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import durdinapps.rxfirebase2.RxFirebaseAuth;
import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

class LoginApiImpl extends BaseApi implements LoginApi {

    private String TAG = "LoginApiImpl";

    @Override
    public Maybe<AuthResult> registerInFirebaseAuth(String email, String password) {
        return RxFirebaseAuth.createUserWithEmailAndPassword(FirebaseAuth.getInstance(), email, password);
    }

    @Override
    public Completable registerInFirebaseDatabase(User user){
        Log.d(TAG, "register in fire-base database");
        return RxFirebaseDatabase.setValue(FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY).child(user.getId()), user);
    }

    @Override
    public Maybe<AuthResult> login(String email, String password) {
        return RxFirebaseAuth.signInWithEmailAndPassword(FirebaseAuth.getInstance(), email, password);
    }

}
