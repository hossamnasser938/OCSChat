package com.example.android.ocschat.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.activity.HomeActivity
import com.example.android.ocschat.model.UserState
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.viewModel.LoginViewModel
import com.google.firebase.storage.FirebaseStorage
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_register_more_info.*
import java.io.IOException
import javax.inject.Inject

class RegisterMoreInfoFragment : Fragment() {

    private val TAG = "RegisterMoreFragment"

    @Inject
    lateinit var loginViewModel : LoginViewModel

    lateinit var storage : FirebaseStorage

    private lateinit var disposable : Disposable
    private lateinit var transient: LoginFragment.LoginTransitionInterface

    private var filePath: Uri? = null

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
        storage = FirebaseStorage.getInstance()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register_more_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get inputs received from Register fragment as arguments
        val userInputs = arguments?.getSerializable(Constants.INPUTS_KEY) as HashMap<String, Any>

        setChooseProfilePictureClickListener()
        setClickLoginOnClickListener()
        setRegisterButtonClickListener(userInputs)
    }

    override fun onPause() {
        super.onPause()

        try { disposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { //postponed
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
                register_user_image_view.setImageBitmap(bitmap)
                register_choose_image_text_view.visibility = View.GONE
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    private fun setChooseProfilePictureClickListener(){
        register_user_image_view.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Select Image"), Constants.PICK_IMAGE_REQUEST)
        }
    }

    private fun setRegisterButtonClickListener(inputs : HashMap<String, Any>){
        register_button.setOnClickListener{
            getUserInputs(inputs)

            //Call web service
            callRegisterApi(inputs)
            uploadImage()
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
            intent.putExtra(Constants.USER_STATE_KEY, UserState.JUST_REGISTERED)
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

    private fun uploadImage(){
        Log.d(TAG, "upload image")
        if(filePath != null){
            Log.d(TAG, "file path is not null")
            val storageRef = storage.reference.child("user_images/" + filePath!!.lastPathSegment)
            val uploadTask = storageRef.putFile(filePath!!)
            uploadTask.continueWithTask{

                if(it.isSuccessful){
                    it.exception
                }
                storageRef.downloadUrl

            }.addOnCompleteListener {
                if(it.isSuccessful){
                    Log.d(TAG, "success")
                    Log.d(TAG, "download uri = " + it.result)
                }
                else{
                    //handle failure
                    Log.d(TAG, "failure")
                }
            }
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