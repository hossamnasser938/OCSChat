package com.example.android.ocschat.api.impl;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.ocschat.util.Constants;
import com.example.android.ocschat.util.OCSChatThrowable;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

import durdinapps.rxfirebase2.RxFirebaseDatabase;
import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

public class BaseApi {

    private static final String TAG = "BaseApi";

    public Maybe<DataSnapshot> getUser(String userId) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(Constants.USERS_KEY).child(userId);
        return RxFirebaseDatabase.observeSingleValueEvent(userRef);
    }

    /**
     * downloads an image from fire-base storage
     * @param downloadUrl the url of the image file to be downloaded
     * @return the file saved locally
     */
    public Single<File> downloadImage(final Uri downloadUrl, final String userId){
        if(downloadUrl != null){
            Log.d(TAG, "downloadUrl is not null");
            return Single.create(new SingleOnSubscribe<File>() {
                @Override
                public void subscribe(final SingleEmitter<File> emitter) throws Exception {
                    FirebaseStorage storage = FirebaseStorage.getInstance();

                    StorageReference storageRef = storage.getReferenceFromUrl(downloadUrl.toString());

                    File localFile = null;
                    try {
                        localFile = File.createTempFile(userId, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                        emitter.onError(e);
                    }

                    if(localFile != null){
                        final File createdFile = localFile;
                        storageRef.getFile(createdFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Log.d(TAG, "successfully downloaded image as local file");
                                emitter.onSuccess(createdFile);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "failed downloadeding image as local file");
                                emitter.onError(e);
                            }
                        });
                    }
                }
            });
        }
        else{
            Log.d(TAG, "downloadUrl is null");
            return Single.error(new OCSChatThrowable(Constants.NULL_DOWNLOAD_URLH));
        }
    }

    /**
     * uploads user image to fire-base cloud storage and returns download url
     * @param filePath
     * @return
     */
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
