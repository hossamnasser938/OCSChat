package com.example.android.ocschat.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.activity.HomeActivity
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.viewModel.LoginViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_register_more_info.*
import javax.inject.Inject

class RegisterMoreInfoFragment : Fragment() {

    @Inject
    lateinit var loginViewModel : LoginViewModel
    private lateinit var disposable : Disposable
    private lateinit var transient: LoginFragment.LoginTransitionInterface

    companion object {
        fun newInstance(map : HashMap<String, Any>) : RegisterMoreInfoFragment{
            val registerMoreInfoFragment = RegisterMoreInfoFragment()
            val args = Bundle()
            args.putSerializable(Constants.INPUTS_KEY, map)
            registerMoreInfoFragment.arguments = args
            return registerMoreInfoFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
        transient = activity as LoginFragment.LoginTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register_more_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get inputs received from Register fragment as arguments
        val userInputs = arguments?.getSerializable(Constants.INPUTS_KEY) as HashMap<String, Any>

        setClickLoginOnClickListener()
        setRegisterButtonClickListener(userInputs)
    }

    override fun onPause() {
        super.onPause()

        try { disposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }
    }

    private fun setRegisterButtonClickListener(inputs : HashMap<String, Any>){
        register_button.setOnClickListener{
            getUserInputs(inputs)

            //Call web service
            callRegisterApi(inputs)
        }
    }

    private fun getUserInputs(inputs : HashMap<String, Any>){
        if(!register_age_edit_text.text.toString().isEmpty())
            inputs[Constants.AGE_KEY] = register_age_edit_text.text.toString().toInt()
        if(!register_education_edit_text.text.toString().isEmpty())
            inputs[Constants.EDUCATION_KEY] = register_education_edit_text.text.toString()
        if(!register_education_org_edit_text.text.toString().isEmpty())
            inputs[Constants.EDUCATION_ORG_KEY] = register_education_org_edit_text.text.toString()
        if(!register_major_edit_text.text.toString().isEmpty())
            inputs[Constants.MAJOR_KEY] = register_major_edit_text.text.toString()
        if(!register_work_edit_text.text.toString().isEmpty())
            inputs[Constants.WORK_KEY] = register_work_edit_text.text.toString()
        if(!register_company_edit_text.text.toString().isEmpty())
            inputs[Constants.COMPANY_KEY] = register_company_edit_text.text.toString()
    }

    private fun callRegisterApi(body : HashMap<String, Any>){
        //show loading progress bar
        register_loading_progress_bar.visibility = View.VISIBLE

        disposable = loginViewModel.register(body).subscribe({
            //hide loading progress bar
            register_loading_progress_bar.visibility = View.GONE
            //Open Home Activity with user info
            val intent = Intent(context, HomeActivity::class.java)
            startActivity(intent)
            Toast.makeText(context, getString(R.string.welcome) + body[Constants.FIRST_NAME_KEY], Toast.LENGTH_SHORT).show()
            activity?.finish()
        }, {
            Log.d("RegisterMoreFragment", it.message)
            //hide loading progress bar
            register_loading_progress_bar.visibility = View.GONE
            //show error message
            showErrorMessage(Constants.FAILED_REGISTERING_MESSAGE)
        })
    }

    private fun setClickLoginOnClickListener(){
        click_login_text_view.setOnClickListener{
            transient.openFragment(LoginFragment())
        }
    }

    /**
     * show error message to user
     */
    private fun showErrorMessage(messageId : Int){
        register_error_text_view.visibility = View.VISIBLE
        register_error_text_view.text = getString(messageId)
        register_button.isClickable = true
    }

    private fun showErrorMessage(message : String?){
        register_error_text_view.visibility = View.VISIBLE
        register_error_text_view.text = message
        register_button.isClickable = true
    }
}