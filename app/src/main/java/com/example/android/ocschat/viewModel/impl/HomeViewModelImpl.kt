package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.model.User
import com.example.android.ocschat.model.UserState
import com.example.android.ocschat.repository.HomeRepository
import com.example.android.ocschat.viewModel.HomeViewModel
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class
HomeViewModelImpl : HomeViewModel {

    private val repository : HomeRepository

    constructor(repository : HomeRepository) {
        this.repository = repository
    }


    override fun getCurrentUserFriends(userState: UserState) : Flowable<User> {
        when(userState){
            UserState.JUST_LOGGED -> {
                return repository.justLoggedUserFriends
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
            UserState.LOGGED_BEFORE, UserState.JUST_REGISTERED -> {
                return repository.currentUserFriends
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
            }
        }
    }

    override fun clearDatabase() {
        repository.clearDatabase()
    }
}