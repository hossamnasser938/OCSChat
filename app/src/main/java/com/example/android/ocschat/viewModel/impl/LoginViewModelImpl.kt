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
import java.util.*


class LoginViewModelImpl : LoginViewModel {

    private val api : LoginApi

    constructor(api: LoginApi) {
        this.api = api
    }

    override fun register(body: HashMap<String, Any>): Maybe<FirebaseUser> {
        return api.registerInFirebaseAuth(body[Constants.EMAIL_KEY] as String, body[Constants.PASSWORD_KEY] as String)
                .flatMap { AuthResult ->
            if(AuthResult.user != null) {
                val user = User(AuthResult.user.uid, body[Constants.FIRST_NAME_KEY] as String, body[Constants.LAST_NAME_KEY] as String)
                addUserInfo(user, body)
                api.registerInFirebaseDatabase(user)
                        .subscribe({  }, { Maybe.error<OCSChatThrowable>(OCSChatThrowable(it.message)) })
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

    private fun addUserInfo(user : User, body: HashMap<String, Any>){
        if(body.containsKey(Constants.AGE_KEY))
            user.age = body[Constants.AGE_KEY] as Int
        if(body.containsKey(Constants.EDUCATION_KEY))
            user.education = body[Constants.EDUCATION_KEY] as String
        if(body.containsKey(Constants.EDUCATION_ORG_KEY))
            user.educationOrganization = body[Constants.EDUCATION_ORG_KEY] as String
        if(body.containsKey(Constants.MAJOR_KEY))
            user.major = body[Constants.MAJOR_KEY] as String
        if(body.containsKey(Constants.WORK_KEY))
            user.work = body[Constants.WORK_KEY] as String
        if(body.containsKey(Constants.COMPANY_KEY))
            user.company = body[Constants.COMPANY_KEY] as String
    }
}