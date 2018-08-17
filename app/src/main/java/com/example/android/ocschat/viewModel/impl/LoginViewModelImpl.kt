package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.dataLayer.LoginApi
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


    override fun register(email: String?, name: String?, password: String?): Maybe<FirebaseUser> {
        return api.register(email, name, password).flatMap { AuthResult ->
            if(AuthResult.user != null) Maybe.just(AuthResult.user)
            else Maybe.error(OCSChatThrowable(Constants.FAILED_REGISTERING_MESSAGE)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun login(email: String?, password: String?): Maybe<FirebaseUser> {
        return api.login(email, password).flatMap { AuthResult ->
            if(AuthResult.user != null) Maybe.just(AuthResult.user)
            else Maybe.error(OCSChatThrowable(Constants.FAILED_Login_MESSAGE)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

}