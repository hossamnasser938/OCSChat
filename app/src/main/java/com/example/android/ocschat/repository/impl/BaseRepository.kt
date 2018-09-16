package com.example.android.ocschat.repository.impl

import com.example.android.ocschat.localDatabase.Gate
import com.example.android.ocschat.model.User
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Flowable
import io.reactivex.Single

open class BaseRepository(private val gate : Gate) {

    fun getUser(userid: String?): Single<User> {
        return gate.getUser(userid)
    }

    fun getCurrentUserFriends(): Flowable<User> {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        return gate.getUserFriends(currentUserId)
    }

}