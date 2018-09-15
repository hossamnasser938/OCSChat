package com.example.android.ocschat.fragment

import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.activity.HomeActivity
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.util.Utils
import com.example.android.ocschat.viewModel.LoginViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import org.eclipse.osgi.framework.adaptor.FilePath
import java.io.IOException
import javax.inject.Inject

class RegisterFragment : Fragment() {

    @Inject
    lateinit var loginViewModel : LoginViewModel

    private lateinit var transient: LoginFragment.LoginTransitionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
        transient = activity as LoginFragment.LoginTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setNextButtonOnClickListener()
        setClickLoginOnClickListener()
    }

    private fun setNextButtonOnClickListener(){
        next_button.setOnClickListener {
            //Do not respond to user clicks for now
            it.isClickable = false
            //hide error text view
            register_error_text_view.visibility = View.GONE

            //check internet connection
            if(!Utils.isNetworkConnected(context)){
                showErrorMessage(R.string.no_internet_connection)
                return@setOnClickListener
            }

            //get email and password entered by user
            val userInput = getUserInput()

            //check empty email or password
            if(!loginViewModel.checkEmptyInputs(userInput[Constants.EMAIL_KEY] as String, userInput[Constants.FIRST_NAME_KEY] as String, userInput[Constants.LAST_NAME_KEY] as String, userInput[Constants.PASSWORD_KEY] as String)){
                showErrorMessage(R.string.forgot_email_password_name)
                return@setOnClickListener
            }

            //check email validity
            if(!Utils.isValidEmail(userInput[Constants.EMAIL_KEY] as String)){
                showErrorMessage(R.string.invalid_email)
                return@setOnClickListener
            }

            //check first name validity
            if(!Utils.isValidName(userInput[Constants.FIRST_NAME_KEY] as String)){
                showErrorMessage(R.string.invalid_name)
                return@setOnClickListener
            }

            //check last name validity
            if(!Utils.isValidName(userInput[Constants.LAST_NAME_KEY] as String)){
                showErrorMessage(R.string.invalid_name)
                return@setOnClickListener
            }

            //check password validity
            if(!Utils.isValidPassword(userInput[Constants.PASSWORD_KEY] as String)){
                showErrorMessage(R.string.invalid_password)
                return@setOnClickListener
            }

            performNext(userInput)
        }
    }

    private fun performNext(inputs : HashMap<String, Any>){
        transient.openFragment(RegisterMoreInfoFragment.newInstance(inputs))
    }

    private fun setClickLoginOnClickListener(){
        click_login_text_view.setOnClickListener{
            transient.openFragment(LoginFragment())
        }
    }


    /**
     * extract user inputs in a HashMap
     */
    private fun getUserInput() : HashMap<String, Any>{
        val userInput : HashMap<String, Any> = HashMap()
        userInput[Constants.EMAIL_KEY] = register_email_edit_text.text.toString()
        userInput[Constants.FIRST_NAME_KEY] = register_firstname_edit_text.text.toString()
        userInput[Constants.LAST_NAME_KEY] = register_lastname_edit_text.text.toString()
        userInput[Constants.PASSWORD_KEY] = register_password_edit_text.text.toString()
        return userInput
    }

    /**
     * show error message to user
     */
    private fun showErrorMessage(messageId : Int){
        register_error_text_view.visibility = View.VISIBLE
        register_error_text_view.text = getString(messageId)
        next_button.isClickable = true
    }

    private fun showErrorMessage(message : String?){
        register_error_text_view.visibility = View.VISIBLE
        register_error_text_view.text = message
        next_button.isClickable = true
    }

}