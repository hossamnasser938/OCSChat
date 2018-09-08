package com.example.android.ocschat.repository.impl

import com.example.android.ocschat.api.AddFriendApi
import com.example.android.ocschat.localDatabase.Gate
import com.example.android.ocschat.model.Friend
import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.AddFriendRepository
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.*

class AddFriendRepositoryImpl : AddFriendRepository {

    private val gate : Gate
    private val api : AddFriendApi

    constructor(gate: Gate, api: AddFriendApi) {
        this.gate = gate
        this.api = api
    }

    override fun getCurrentUserFriends(): Flowable<User> {
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

        return gate.getUserFriends(currentUserId)
    }

    override fun getCurrentUserNonFriends(): Flowable<User> {
        //create custom flowable that emits non friend users and ignore friends ones
        return api.allUsers.flatMap {
            val user = it.value.getValue(User::class.java)
            gate.isFriend(user.id).flatMapPublisher {
                if(!it)
                    Flowable.just(user)
                else
                    Flowable.just(null)
            }
        }
    }

    override fun addFriend(friend: Friend?): Completable {
        return gate.addFriend(friend)
                .concatWith(api.addFriend(friend))
    }

    override fun isFriend(friendID: String?): Single<Boolean> {
        return gate.isFriend(friendID)
    }
}