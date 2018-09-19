package com.example.android.ocschat.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
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
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_add_profile_photo.*
import java.io.IOException
import javax.inject.Inject

class AddPhotoFragment : Fragment() {

    private val TAG = "AddPhotoFragment"

    @Inject
    lateinit var loginViewModel : LoginViewModel

    private lateinit var disposable : Disposable
    private lateinit var transient: LoginFragment.LoginTransitionInterface

    private lateinit var filePath: Uri

    companion object {
        fun newInstance(map : HashMap<String, Any>) : AddPhotoFragment{
            val addPhotoFragment = AddPhotoFragment()
            val args = Bundle()
            args.putSerializable(Constants.INPUTS_KEY, map)
            addPhotoFragment.arguments = args
            return addPhotoFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
        transient = activity as LoginFragment.LoginTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_profile_photo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get inputs received from Register fragment as arguments
        val userInputs = arguments?.getSerializable(Constants.INPUTS_KEY) as HashMap<String, Any>

        setProfilePicturePlaceholder()
        setChooseProfilePictureClickListener()
        setAddClickListener(userInputs)
        setSkipClickListener(userInputs)
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.add_profile_picture)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
                val drawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
                drawable.isCircular = true
                add_photo_image_view.setImageDrawable(drawable)
                add_photo_choose_image_text_view.visibility = View.GONE
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun setProfilePicturePlaceholder(){
        val bitmap = BitmapFactory.decodeResource(context?.resources, R.drawable.person_placeholder)
        Log.d(TAG, "bitmap " + bitmap.byteCount)
        val drawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
        Log.d(TAG, "drawable " + drawable.toString())
        drawable.isCircular = true
        add_photo_image_view.setImageDrawable(drawable)
    }

    private fun setChooseProfilePictureClickListener(){
        add_photo_image_view.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT  //ACTION_OPEN_DOCUMENT instead of ACTION_GET_DOCUMENT to have future access to this file
            startActivityForResult(Intent.createChooser(intent, "Select Image"), Constants.PICK_IMAGE_REQUEST)
        }
    }

    private fun setAddClickListener(body : HashMap<String, Any>){
        add_photo_add_button.setOnClickListener{
            try{
                body[Constants.FILE_PATH_KEY] = filePath
                Log.d(TAG, "file path exists")
                callRegisterApi(body)
            }
            catch (e : UninitializedPropertyAccessException){
                Log.d(TAG, "no file path")
                //file path has not been initialized
                showErrorMessage(R.string.chosen_no_image)
            }
        }
    }

    private fun setSkipClickListener(body : HashMap<String, Any>){
        add_photo_skip_button.setOnClickListener{
            try{
                body[Constants.FILE_PATH_KEY] = filePath
                Log.d(TAG, "file path exists")
                showErrorMessage(R.string.chosen_image)
            }
            catch (e : UninitializedPropertyAccessException){
                Log.d(TAG, "no file path")
                //file path has not been initialized
                callRegisterApi(body)
            }
        }
    }

    private fun callRegisterApi(body : HashMap<String, Any>){
        //show loading progress bar
        add_photo_loading_progress_bar.visibility = View.VISIBLE

        disposable = loginViewModel.registerInRealtimeDatabase(body).subscribe({
            //hide loading progress bar
            add_photo_loading_progress_bar.visibility = View.GONE
            //Open Home Activity with user info
            val intent = Intent(context, HomeActivity::class.java)
            intent.putExtra(Constants.USER_STATE_KEY, UserState.JUST_REGISTERED)
            startActivity(intent)
            Toast.makeText(context, getString(R.string.welcome) + body[Constants.FIRST_NAME_KEY], Toast.LENGTH_SHORT).show()
            activity?.finish()
        }, {
            Log.d(TAG, it.message)
            //hide loading progress bar
            add_photo_loading_progress_bar.visibility = View.GONE
            //show error message
            showErrorMessage(Constants.FAILED_REGISTERING_MESSAGE)
        })
    }

    /**
     * show error message to user
     */
    private fun showErrorMessage(messageId : Int){
        add_photo_error_text.visibility = View.VISIBLE
        add_photo_error_text.text = getString(messageId)
    }

    private fun showErrorMessage(message : String?){
        add_photo_error_text.visibility = View.VISIBLE
        add_photo_error_text.text = message
    }

}