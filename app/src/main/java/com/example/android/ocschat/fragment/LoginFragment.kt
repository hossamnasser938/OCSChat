package com.example.android.ocschat.fragment

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
import javax.inject.Inject


class LoginFragment : Fragment() {

    @Inject
    lateinit var loginViewModel : LoginViewModel
    private lateinit var disposable : Disposable
    private lateinit var transient: LoginTransitionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
        transient = activity as LoginTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLoginButtonOnClickListener()
        setClickRegisterOnClickListener()
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

    private fun setLoginButtonOnClickListener(){
        login_button.setOnClickListener {
            Log.d("LoginFragment", "login button clicked")
            //Do not respond to user clicks for now
            it.isClickable = false
            //hide error text view
            login_error_text_view.visibility = View.GONE

            //check internet connection
            if(!Utils.isNetworkConnected(context)){
                showErrorMessage(R.string.no_internet_connection)
                return@setOnClickListener
            }

            //get email and password entered by user
            val userInput : HashMap<String, String> = getUserInput()

            //check empty email or password
            if(!loginViewModel.checkEmptyInputs(userInput[Constants.EMAIL_KEY], userInput[Constants.PASSWORD_KEY])){
                showErrorMessage(R.string.forgot_email_password)
                return@setOnClickListener
            }

            //check email validity
            if(!loginViewModel.isValidEmail(userInput[Constants.EMAIL_KEY])){
                showErrorMessage(R.string.invalid_email)
                return@setOnClickListener
            }

            //check password validity
            if(!loginViewModel.isValidPassword(userInput[Constants.PASSWORD_KEY])){
                showErrorMessage(R.string.invalid_password)
                return@setOnClickListener
            }
            //show loading progress bar
            login_loading_progress_bar.visibility = View.VISIBLE

            //Call web service
            callLoginApi(userInput)
        }
    }

    private fun callLoginApi(body : HashMap<String, String>){
        disposable = loginViewModel.login(body).subscribe({
            //hide loading progress bar
            login_loading_progress_bar.visibility = View.GONE
            //Open Home Activity with user info
            val intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }, {
            //hide loading progress bar
            login_loading_progress_bar.visibility = View.GONE
            //show error message
            showErrorMessage(it.message)
        })
    }

    fun setClickRegisterOnClickListener(){
        click_register_text_view.setOnClickListener{
            transient.openFragment(RegisterFragment())
        }
    }

    /**
     * extract email and password of user in a HashMap
     */
    private fun getUserInput() : HashMap<String, String>{
        val userInput : HashMap<String, String> = HashMap()
        userInput[Constants.EMAIL_KEY] = login_email_edit_text.text.toString()
        userInput[Constants.PASSWORD_KEY] = login_password_edit_text.text.toString()
        return userInput
    }

    /**
     * show error message to user
     */
    private fun showErrorMessage(messageId : Int){
        login_error_text_view.visibility = View.VISIBLE
        login_error_text_view.text = getString(messageId)
        login_button.isClickable = true
    }

    private fun showErrorMessage(message : String?){
        login_error_text_view.visibility = View.VISIBLE
        login_error_text_view.text = message
        login_button.isClickable = true
    }

    interface LoginTransitionInterface{
        fun openFragment(fragment : Fragment)
    }

}