package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.dataLayer.LoginApi
import com.example.android.ocschat.model.User
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.util.OCSChatThrowable
import com.example.android.ocschat.viewModel.LoginViewModel
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Maybe
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers;


class LoginViewModelImpl : LoginViewModel {

    private val api : LoginApi

    constructor(api: LoginApi) {
        this.api = api
    }

    override fun register(body: HashMap<String, String>): Maybe<FirebaseUser> {
        return api.registerInFirebaseAuth(body[Constants.EMAIL_KEY], body[Constants.PASSWORD_KEY])
                .flatMap { AuthResult ->
            if(AuthResult.user != null) {
                api.registerInFirebaseDatabase(User(AuthResult.user.uid, body[Constants.NAME_KEY]))
                        .subscribe({  }, { Maybe.error<OCSChatThrowable>(OCSChatThrowable(it.message)) })//removed: Constants.FAILED_REGISTERING_MESSAGE
                Maybe.just(AuthResult.user)
            }
            else Maybe.error(OCSChatThrowable(Constants.FAILED_REGISTERING_MESSAGE)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun login(body : HashMap<String, String>): Maybe<FirebaseUser> {
        return api.login(body[Constants.EMAIL_KEY], body[Constants.PASSWORD_KEY]).flatMap { AuthResult ->
            if(AuthResult.user != null) Maybe.just(AuthResult.user)
            else Maybe.error(OCSChatThrowable(Constants.FAILED_LOGIN_MESSAGE)) }
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