package com.example.android.ocschat.api.impl;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.ocschat.util.Constants;
import com.example.android.ocschat.util.OCSChatThrowable;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
     * @return the Uri of the file saved locally
     */
    public Single<Uri> downloadImage(final Uri downloadUrl, final String userId){
        if(downloadUrl != null){
            Log.d(TAG, "downloadUrl is not null");
            return Single.create(new SingleOnSubscribe<Uri>() {
                @Override
                public void subscribe(final SingleEmitter<Uri> emitter) throws Exception {
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
                                emitter.onSuccess(Uri.fromFile(createdFile));
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
}
