package com.example.android.ocschat.viewModel.impl

import android.net.Uri
import android.util.Log
import com.example.android.ocschat.repository.LoginRepository import com.example.android.ocschat.viewModel.LoginViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.util.*


class LoginViewModelImpl(private val repository: LoginRepository) : LoginViewModel {

    private val TAG = "LoginViewModel"

    override fun register(body: HashMap<String, Any>): Completable {
        return repository.register(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun login(body : HashMap<String, String>): Maybe<FirebaseUser> {
        return repository.login(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun uploadImage(filePath: Uri?): Single<Uri> {
        return repository.uploadImage(filePath)

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