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
        if(userState == UserState.JUST_LOGGED){
               return repository.justLoggedUserFriends
                       .subscribeOn(Schedulers.io())
                       .observeOn(AndroidSchedulers.mainThread())
        }
        else{
            //Default: UserState.LOGGED_BEFORE
            return repository.currentUserFriends
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun userExists(friendsList: List<User>, user : User) :Boolean{
        if(friendsList.isNotEmpty()){
            friendsList.forEach {
                if(it.id.equals(user.id, false))
                    return true
            }
        }
        return false
    }

    override fun clearDatabase() {
        repository.clearDatabase()
    }
}