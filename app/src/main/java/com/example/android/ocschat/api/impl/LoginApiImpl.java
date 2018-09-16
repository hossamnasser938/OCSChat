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

class LoginApiImpl implements LoginApi {

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

    @Override
    public Maybe<DataSnapshot> getUser(String uid){
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY).child(uid);
        return RxFirebaseDatabase.observeSingleValueEvent(userReference);
    }

    /**
     * uploads user image to fire-base cloud storage and returns download url
     * @param filePath
     * @return
     */
    @Override
    public Single<Uri> uploadImage(Uri filePath) {
        Log.d(TAG, "upload image");
        final Uri fPath = filePath;
        final FirebaseStorage storage = FirebaseStorage.getInstance();

        if(fPath != null) {
            Log.d(TAG, "file path is not null");
            return Single.create(new SingleOnSubscribe<Uri>() {
                @Override
                public void subscribe(final SingleEmitter<Uri> emitter) {
                    final StorageReference storageRef = storage.getReference().child("user_images/" + fPath.getLastPathSegment());
                    UploadTask uploadTask = storageRef.putFile(fPath);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful()){
                                throw  task.getException();
                            }
                            return storageRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()) {
                                Log.d(TAG, "successfully got download Uri");
                                Uri downloadUri = task.getResult();
                                Log.d(TAG, "Download Uri : " + downloadUri);
                                emitter.onSuccess(downloadUri);
                            }
                            else {
                                Log.d(TAG, "error task is not successful");
                                emitter.onError(new OCSChatThrowable(Constants.ERROR_UPLOADING_IMAGE));
                            }
                        }
                    });
                }
            });
        }
        else {
            Log.d(TAG, "file path is null");
            return Single.error(new OCSChatThrowable(Constants.NULL_FILE_PATH));
        }
    }

}
