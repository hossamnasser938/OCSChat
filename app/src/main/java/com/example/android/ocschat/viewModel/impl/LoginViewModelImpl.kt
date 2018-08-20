package com.example.android.ocschat.viewModel.impl

import com.example.android.ocschat.dataLayer.LoginApi
import com.example.android.ocschat.model.Friend
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
                //testing start
                var user = User(AuthResult.user.uid, body[Constants.NAME_KEY])
                user.addFriend(Friend("1"))
                user.addFriend(Friend("2"))
                user.addFriend(Friend("3"))
                api.registerInFirebaseDatabase(user)
                        //testing finish
                //api.registerInFirebaseDatabase(User(AuthResult.user.uid, body[Constants.NAME_KEY]))
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