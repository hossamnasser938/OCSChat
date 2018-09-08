package com.example.android.ocschat.repository.impl

import com.example.android.ocschat.localDatabase.Gate
import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.HomeRepository
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.Flowable

class HomeRepositoryImpl : HomeRepository {

    private val gate : Gate

    constructor(gate: Gate) {
        this.gate = gate
    }


    override fun getCurrentUserFriends(): Flowable<User> {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        return gate.getUserFriends(currentUserId)
    }
}