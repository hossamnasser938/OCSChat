package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.repository.LoginRepository import com.example.android.ocschat.viewModel.LoginViewModel
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.util.*


class LoginViewModelImpl(private val repository: LoginRepository) : LoginViewModel {

    private val TAG = "LoginViewModel"

    override fun registerInAuth(body: HashMap<String, Any>?): Maybe<AuthResult> {
        return repository.registerInAuth(body)
    }

    override fun registerInRealtimeDatabase(body: HashMap<String, Any>?): Completable {
        return repository.registerInRealtimeDatabase(body)
    }

    override fun login(body : HashMap<String, String>): Maybe<FirebaseUser> {
        return repository.login(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Checks if one of user inputs is empty
     */
    override fun checkEmptyInputs(vararg inputs: String): Boolean {
        for(input in inputs){
            if(input.isEmpty())
                return false
        }
        return true
    }
}