package com.example.android.ocschat.repository.impl

import android.util.Log
import com.example.android.ocschat.api.AddFriendApi
import com.example.android.ocschat.api.impl.BaseApi
import com.example.android.ocschat.localDatabase.Gate
import com.example.android.ocschat.model.Friend
import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.AddFriendRepository
import io.reactivex.*

class AddFriendRepositoryImpl(private val gate: Gate, private val api: AddFriendApi, private val baseApi : BaseApi) : BaseRepository(gate, baseApi), AddFriendRepository {

    private val TAG = "AddFriendRepoImpl"

    override fun getCurrentUserNonFriends(): Flowable<User> {
        //create custom flowable that emits non friend users and ignore friends ones
        Log.d(TAG, "got into api fetching")
        return api.allUsers.flatMap {
            val user = it.value.getValue(User::class.java)
            Log.d(TAG, "got friend from api: " + user.firstName)
            gate.isFriend(user.id).flatMapPublisher {
                if(!it)
                    Flowable.just(user)
                else {
                    Flowable.empty()
                }
            }
        }
    }

    override fun addFriend(user: User?): Completable {
        return gate.addFriend(user)
                .concatWith(api.addFriend(Friend(user?.id)))
    }

    override fun isFriend(friendID: String?): Single<Boolean> {
        return gate.isFriend(friendID)
    }
}