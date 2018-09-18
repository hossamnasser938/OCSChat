package com.example.android.ocschat.repository.impl

import android.net.Uri
import android.util.Log
import com.example.android.ocschat.api.LoginApi
import com.example.android.ocschat.api.impl.BaseApi
import com.example.android.ocschat.localDatabase.Gate
import com.example.android.ocschat.model.User
import com.example.android.ocschat.repository.LoginRepository
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.util.OCSChatThrowable
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.HashMap

class LoginRepositoryImpl(private val gate: Gate, private val api: LoginApi, private val baseApi : BaseApi) : BaseRepository(gate, baseApi), LoginRepository {

    private val TAG = "LoginRepositoryImpl"

    override fun registerInAuth(body: HashMap<String, Any>): Maybe<AuthResult> {
        return api.registerInFirebaseAuth(body[Constants.EMAIL_KEY] as String, body[Constants.PASSWORD_KEY] as String)
    }

    override fun registerInRealtimeDatabase(body: HashMap<String, Any>): Completable {
        val user = User(body[Constants.ID_KEY] as String, body[Constants.FIRST_NAME_KEY] as String, body[Constants.LAST_NAME_KEY] as String)
        addUserInfo(user, body)
        if(body.containsKey(Constants.FILE_PATH_KEY)){
            Log.d(TAG, "user with image")
            return api.uploadImage(body[Constants.FILE_PATH_KEY] as Uri)
                    .flatMapCompletable {
                        Log.d(TAG, "successfully uploaded image")
                        user.hasImage = true
                        user.imageUrl = it.toString()
                        Log.d(TAG, "set user has image")
                        api.registerInFirebaseDatabase(user)
                                .concatWith{ observer ->
                                    Log.d(TAG, "successfully registered in real-time database")
                                    user.imageFilePath = (body[Constants.FILE_PATH_KEY] as Uri).toString()
                                    gate.insertUser(user).subscribe ({
                                        observer.onComplete()
                                    }, {
                                        observer.onError(it)
                                    })
                                    }
                    }
        }
        else{
            Log.d(TAG, "user with no image")
            return api.registerInFirebaseDatabase(user)
                    .concatWith(gate.insertUser(user))
        }
    }

    override fun login(body : HashMap<String, String>): Maybe<FirebaseUser> {
        return api.login(body[Constants.EMAIL_KEY], body[Constants.PASSWORD_KEY]).flatMap { authResult ->
            if(authResult.user != null) {
                Log.d(TAG, "logged successfully")
                api.getUser(authResult.user.uid).flatMap { dataSnapshot ->
                    Log.d(TAG, "got user from api successfully")
                    val user = dataSnapshot.getValue(User::class.java)
                    if (user.hasImage) {
                        Log.d(TAG, "user has image")
                        api.downloadImage(Uri.parse(user.imageUrl), user.id)
                                .flatMapMaybe {
                                    Log.d(TAG, "image downloaded successfully")
                                    user.imageFilePath = Uri.fromFile(it).toString()
                                    gate.insertUser(user)
                                            .andThen(Maybe.just(authResult.user))
                                }
                    } else {
                        Log.d(TAG, "user has no image")
                        gate.insertUser(user)
                                .andThen(Maybe.just(authResult.user))
                    }
                }
            }
            else {
                Log.d(TAG, "error logging user")
                Maybe.error(OCSChatThrowable(Constants.FAILED_LOGIN_MESSAGE))
            } }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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