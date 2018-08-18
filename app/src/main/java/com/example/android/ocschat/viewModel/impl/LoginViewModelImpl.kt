package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.dataLayer.LoginApi
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.util.OCSChatThrowable
import com.example.android.ocschat.viewModel.LoginViewModel
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer

class LoginViewModelImpl : LoginViewModel {

    private val api : LoginApi

    constructor(api: LoginApi) {
        this.api = api
    }


    override fun register(body: HashMap<String, String>): Maybe<FirebaseUser> {
        return api.registerInFirebaseAuth(body[Constants.EMAIL_KEY], body[Constants.PASSWORD_KEY])
                .flatMap { AuthResult ->
            if(AuthResult.user != null) {
                api.registerInFirebaseDatabase(AuthResult.user.uid, body[Constants.NAME_KEY]).doOnError { Maybe.error<OCSChatThrowable>(OCSChatThrowable(Constants.FAILED_REGISTERING_MESSAGE)) }
                        Maybe.just(AuthResult.user)
            }
            else Maybe.error(OCSChatThrowable(Constants.FAILED_REGISTERING_MESSAGE)) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun login(body : HashMap<String, String>): Maybe<FirebaseUser> {
        return api.login(body[Constants.EMAIL_KEY], body[Constants.PASSWORD_KEY]).flatMap { AuthResult ->
            if(AuthResult.user != null) Maybe.just(AuthResult.user)
            else Maybe.error(OCSChatThrowable(Constants.FAILED_Login_MESSAGE)) }
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

    /**
     * checks if email is in valid format
     * Got from "https://codereview.stackexchange.com/questions/33546/simple-code-to-check-format-of-user-inputted-email-address"
     */
    override fun isValidEmail(email: String): Boolean {
        return email.matches("[a-zA-Z0-9\\.]+@[a-zA-Z0-9\\-\\_\\.]+\\.[a-zA-Z0-9]{3}".toRegex())
    }

    /**
     * Checks if name is valid(starts with a letter)
     */
    override fun isValidName(name: String): Boolean {
        return name[0].isLetter()
    }

    /**
     * checks is password is valid(6 characters or more)
     */
    override fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }
}