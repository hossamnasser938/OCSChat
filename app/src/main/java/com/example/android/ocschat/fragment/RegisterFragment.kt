package com.example.android.ocschat.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import javax.inject.Inject

class RegisterFragment : Fragment() {

    @Inject
    lateinit var loginViewModel : LoginViewModel
    private lateinit var disposable : Disposable
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
        setRegisterButtonOnClickListener()
        setClickLoginOnClickListener()
    }

    override fun onPause() {
        super.onPause()
        try {
            disposable.dispose()
        }
        catch (e : UninitializedPropertyAccessException){
            //Just stop
        }
    }

    private fun setRegisterButtonOnClickListener(){
        val context : Context? = context
        register_button.setOnClickListener {
            //hide error text view
            register_error_text_view.visibility = View.GONE

            //check internet connection
            if(!Utils.isNetworkConnected(context)){
                showErrorMessage(R.string.no_internet_connection)
                return@setOnClickListener
            }

            //get email and password entered by user
            val userInput : HashMap<String, String> = getUserInput()

            //check empty email or password
            if(!loginViewModel.checkEmptyInputs(userInput[Constants.EMAIL_KEY], userInput[Constants.NAME_KEY], userInput[Constants.PASSWORD_KEY])){
                showErrorMessage(R.string.forgot_email_password_name)
                return@setOnClickListener
            }

            //check email validity
            if(!loginViewModel.isValidEmail(userInput[Constants.EMAIL_KEY])){
                showErrorMessage(R.string.invalid_email)
                return@setOnClickListener
            }

            //check name validity
            if(!loginViewModel.isValidName(userInput[Constants.NAME_KEY])){
                showErrorMessage(R.string.invalid_name)
                return@setOnClickListener
            }

            //check password validity
            if(!loginViewModel.isValidPassword(userInput[Constants.PASSWORD_KEY])){
                showErrorMessage(R.string.invalid_password)
                return@setOnClickListener
            }

            //show loading progress bar
            register_loading_progress_bar.visibility = View.VISIBLE

            //Call web service
            callRegisterApi(userInput)
        }
    }

    private fun callRegisterApi(body : HashMap<String, String>){
        disposable = loginViewModel.register(body).subscribe({
            //hide loading progress bar
            register_loading_progress_bar.visibility = View.GONE
            //Open Home Activity with user info
            val intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }, {
            //hide loading progress bar
            register_loading_progress_bar.visibility = View.GONE
            //show error message
            showErrorMessage(it.message)
        })
    }

    fun setClickLoginOnClickListener(){
        click_login_text_view.setOnClickListener{
            transient.openFragment(LoginFragment())
        }
    }

    /**
     * extract email and password of user in a HashMap
     */
    private fun getUserInput() : HashMap<String, String>{
        val userInput : HashMap<String, String> = HashMap()
        userInput[Constants.EMAIL_KEY] = register_email_edit_text.text.toString()
        userInput[Constants.NAME_KEY] = register_username_edit_text.text.toString()
        userInput[Constants.PASSWORD_KEY] = register_password_edit_text.text.toString()
        return userInput
    }

    /**
     * show error message to user
     */
    private fun showErrorMessage(messageId : Int){
        register_error_text_view.visibility = View.VISIBLE
        register_error_text_view.text = getString(messageId)
    }

    private fun showErrorMessage(message : String?){
        login_error_text_view.visibility = View.VISIBLE
        login_error_text_view.text = message
    }

}