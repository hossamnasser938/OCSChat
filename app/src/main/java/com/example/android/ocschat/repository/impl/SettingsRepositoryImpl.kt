package com.example.android.ocschat.repository.impl

import android.net.Uri
import android.util.Log
import com.example.android.ocschat.api.SettingsApi
import com.example.android.ocschat.api.impl.BaseApi
import com.example.android.ocschat.localDatabase.Gate
import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.SettingsRepository
import io.reactivex.Completable

class SettingsRepositoryImpl(private val gate: Gate, private val api: SettingsApi, private val baseApi : BaseApi) : BaseRepository(gate, baseApi), SettingsRepository {

    private val TAG = "SettingsRepositoryImpl"

    override fun updateCurrentUser(user: User, filePath : Uri?): Completable {
        if(filePath == null){
            Log.d(TAG, "user has not updated his picture")
            return gate.updateUser(user)
                    .concatWith(api.updateCurrentUser(user))
        }
        else{
            Log.d(TAG, "user has updated his picture")
            return api.uploadImage(filePath)
                    .flatMapCompletable {
                        Log.d(TAG, "image uploaded successfully")
                        user.hasImage = true
                        user.imageUrl = it.toString()
                        api.updateCurrentUser(user).concatWith { observer ->
                            Log.d(TAG, "user has been updated in api successfully")
                            user.imageFilePath = filePath.toString()
                            gate.updateUser(user).subscribe({
                                observer.onComplete()
                            }, {
                                observer.onError(it)
                            })
                        }
                    }
        }
    }


}