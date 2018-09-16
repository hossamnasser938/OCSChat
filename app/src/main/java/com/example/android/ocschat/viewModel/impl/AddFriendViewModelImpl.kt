package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.AddFriendRepository
import com.example.android.ocschat.repository.impl.BaseRepository
import com.example.android.ocschat.viewModel.AddFriendViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AddFriendViewModelImpl(private val repository: AddFriendRepository, private val baseRepository : BaseRepository) : BaseViewModel(baseRepository), AddFriendViewModel {

    override fun getSuggestedUsers(): Flowable<User> {
        return repository.currentUserFriends
                .concatWith(repository.currentUserNonFriends)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun isFriend(uid: String?): Single<Boolean> {
        return repository.isFriend(uid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun addFriend(user: User?): Completable {
        return repository.addFriend(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}