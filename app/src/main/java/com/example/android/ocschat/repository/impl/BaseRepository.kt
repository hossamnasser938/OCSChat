package com.example.android.ocschat.repository.impl

import android.net.Uri
import com.example.android.ocschat.api.impl.BaseApi
import com.example.android.ocschat.localDatabase.Gate
import com.example.android.ocschat.model.User
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Flowable
import io.reactivex.Single

open class BaseRepository(private val gate : Gate, private val baseApi : BaseApi) {

    fun getUser(userid: String?): Single<User> {
        return gate.getUser(userid)
    }

    fun getCurrentUserFriends(): Flowable<User> {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        return gate.getUserFriends(currentUserId)
    }

    fun downloadImage(downloadUrl : Uri, userId : String) : Single<Uri> {
        return baseApi.downloadImage(downloadUrl, userId)
    }

}