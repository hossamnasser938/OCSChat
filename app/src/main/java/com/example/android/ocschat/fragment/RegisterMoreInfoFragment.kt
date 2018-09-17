package com.example.android.ocschat.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.ocschat.R
import com.example.android.ocschat.util.Constants
import kotlinx.android.synthetic.main.fragment_register_more_info.*

class RegisterMoreInfoFragment : Fragment() {

    private val TAG = "RegisterMoreFragment"

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
        activity?.actionBar?.title = getString(R.string.add_more_info)
        transient = activity as LoginFragment.LoginTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register_more_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get inputs received from Register fragment as arguments
        val userInputs = arguments?.getSerializable(Constants.INPUTS_KEY) as HashMap<String, Any>

        setRegisterButtonClickListener(userInputs)
        setSkipButtonClickListener(userInputs)
    }

    private fun setRegisterButtonClickListener(inputs : HashMap<String, Any>){
        register_button.setOnClickListener{
            if(getUserInputs(inputs)){
                //move to Add Photo fragment with user inputs
                transient.openFragment(AddPhotoFragment.newInstance(inputs))
            }
            else{
                showErrorMessage(R.string.added_no_more_info)
            }
        }
    }

    private fun setSkipButtonClickListener(inputs : HashMap<String, Any>){
        register_skip_button.setOnClickListener{
            if(getUserInputs(inputs)){
                showErrorMessage(R.string.added_more_info)
            }
            else{
                //move to add photo fragment with user inputs
                transient.openFragment(AddPhotoFragment.newInstance(inputs))
            }
        }
    }

    /**
     * get user inputs and check if he added nothing
     */
    private fun getUserInputs(inputs : HashMap<String, Any>) : Boolean{
        var addedInfo = false
        if(!register_age_edit_text.text.toString().isEmpty()) {
            inputs[Constants.AGE_KEY] = register_age_edit_text.text.toString().toInt()
            addedInfo = true
        }
        if(!register_education_edit_text.text.toString().isEmpty()) {
            inputs[Constants.EDUCATION_KEY] = register_education_edit_text.text.toString()
            addedInfo = true
        }
        if(!register_education_org_edit_text.text.toString().isEmpty()) {
            inputs[Constants.EDUCATION_ORG_KEY] = register_education_org_edit_text.text.toString()
            addedInfo = true
        }
        if(!register_major_edit_text.text.toString().isEmpty()) {
            inputs[Constants.MAJOR_KEY] = register_major_edit_text.text.toString()
            addedInfo = true
        }
        if(!register_work_edit_text.text.toString().isEmpty()) {
            inputs[Constants.WORK_KEY] = register_work_edit_text.text.toString()
            addedInfo = true
        }
        if(!register_company_edit_text.text.toString().isEmpty()) {
            inputs[Constants.COMPANY_KEY] = register_company_edit_text.text.toString()
            addedInfo = true
        }
        return addedInfo
    }

    /**
     * show error message to user
     */
    private fun showErrorMessage(messageId : Int){
        register_error_text_view.visibility = View.VISIBLE
        register_error_text_view.text = getString(messageId)
    }

    private fun showErrorMessage(message : String?){
        register_error_text_view.visibility = View.VISIBLE
        register_error_text_view.text = message
    }
}