package com.example.android.ocschat.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.model.User
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.viewModel.SettingsViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_update_profile.*
import java.io.IOException
import javax.inject.Inject

class UpdateProfileFragment : Fragment(){

    private val TAG = "UpdateProfileFragment"

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    private lateinit var filePath: Uri

    lateinit var disposable : Disposable

    private lateinit var currentlyLoggedUser : User

    companion object {
        fun newInstance(user : User) : UpdateProfileFragment{
            val updateProfileFragment = UpdateProfileFragment()
            val args = Bundle()
            args.putSerializable(Constants.USER_KEY, user)
            updateProfileFragment.arguments = args
            return updateProfileFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_update_profile , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentlyLoggedUser = arguments?.get(Constants.USER_KEY) as User
        loadUserInfo()
        setUpdateProfilePictureClickListener()
    }

    override fun onStop() {
        super.onStop()

        try { disposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.update_profile)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.update_profile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.update_menu_item -> {
                //check if user changed something to be updated or not
                if(!checkSomethingUpdated()){
                    Toast.makeText(context, R.string.no_changes_to_update, Toast.LENGTH_SHORT).show()
                    return true
                }

                //Show progress dialog
                val progressDialog = ProgressDialog(activity)
                progressDialog.setTitle(R.string.updating_profile)
                progressDialog.show()

                updateUserProperties()

                //try to access filePath variable to see if it has been initialized which means user has updated his profile picture or not
                var filePathToSend : Uri?
                try{
                    filePathToSend = filePath
                    Log.d(TAG, "not null file path")
                }
                catch (e : UninitializedPropertyAccessException){
                    Log.d(TAG, "null file path")
                    filePathToSend = null  //null means user has not updated his profile picture
                }

                //call fire-base service to update current user info
                disposable = settingsViewModel.updateCurrentUser(currentlyLoggedUser, filePathToSend)
                        .subscribe({
                            //Inform user and go back to Home activity
                            Log.d(TAG, "Updated")
                            Toast.makeText(context, R.string.account_updated, Toast.LENGTH_SHORT).show()
                            progressDialog.dismiss()
                            activity?.finish()
                        }, {
                            Log.d(TAG, it.message)
                            Toast.makeText(context, Constants.ERROR_UPDATING_ACCOUNT, Toast.LENGTH_SHORT).show()
                            progressDialog.dismiss()
                        })

            }
            R.id.cancel_menu_item -> {
                //finish Settings activity and go back to Home activity
                activity?.finish()
            }
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            filePath = data.data
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, filePath)
                update_profile_image_view.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * check if user updated something or not
     */
    private fun checkSomethingUpdated() : Boolean{
        if(!update_profile_firstname_edit_text.text.trim().toString().equals(currentlyLoggedUser.firstName, false))
            return true
        if(!update_profile_lastname_edit_text.text.trim().toString().equals(currentlyLoggedUser.lastName, false))
            return true
        if(update_profile_age_edit_text.visibility == View.VISIBLE){
            if(update_profile_age_edit_text.text.trim().toString().toInt() != currentlyLoggedUser.age)
                return true
        }
        if(update_profile_education_edit_text.visibility == View.VISIBLE){
            if(!update_profile_education_edit_text.text.trim().toString().equals(currentlyLoggedUser.education, false))
                return true
        }
        if(update_profile_education_org_edit_text.visibility == View.VISIBLE){
            if(!update_profile_education_org_edit_text.text.trim().toString().equals(currentlyLoggedUser.educationOrganization, false))
                return true
        }
        if(update_profile_major_edit_text.visibility == View.VISIBLE){
            if(!update_profile_major_edit_text.text.trim().toString().equals(currentlyLoggedUser.major, false))
                return true
        }
        if(update_profile_work_edit_text.visibility == View.VISIBLE){
            if(!update_profile_work_edit_text.text.trim().toString().equals(currentlyLoggedUser.work, false))
                return true
        }
        if(update_profile_company_edit_text.visibility == View.VISIBLE){
            if(!update_profile_company_edit_text.text.trim().toString().equals(currentlyLoggedUser.company, false))
                return true
        }
        //try to access filePath to see if it has been initialized
        try{
            filePath.toString()
            return true
        }
        catch (e : UninitializedPropertyAccessException){ }

        return false
    }

    private fun updateUserProperties() {
        currentlyLoggedUser.firstName = update_profile_firstname_edit_text.text.toString()
        currentlyLoggedUser.lastName = update_profile_lastname_edit_text.text.toString()
        if(update_profile_age_edit_text.visibility == View.VISIBLE)
            currentlyLoggedUser.age = update_profile_age_edit_text.text.toString().toInt()
        if(update_profile_education_edit_text.visibility == View.VISIBLE)
            currentlyLoggedUser.education = update_profile_education_edit_text.text.toString()
        if(update_profile_education_org_edit_text.visibility == View.VISIBLE)
            currentlyLoggedUser.educationOrganization = update_profile_education_org_edit_text.text.toString()
        if(update_profile_major_edit_text.visibility == View.VISIBLE)
            currentlyLoggedUser.major = update_profile_major_edit_text.text.toString()
        if(update_profile_work_edit_text.visibility == View.VISIBLE)
            currentlyLoggedUser.work = update_profile_work_edit_text.text.toString()
        if(update_profile_company_edit_text.visibility == View.VISIBLE)
            currentlyLoggedUser.company = update_profile_company_edit_text.text.toString()
    }


    private fun loadUserInfo(){
        update_profile_firstname_edit_text.setText(currentlyLoggedUser.firstName)
        update_profile_lastname_edit_text.setText(currentlyLoggedUser.lastName)
        if(currentlyLoggedUser.age != null){
            update_profile_age_edit_text.visibility = View.VISIBLE
            update_profile_age_edit_text.setText(currentlyLoggedUser.age.toString())
        }
        if(currentlyLoggedUser.education != null){
            update_profile_education_edit_text.visibility = View.VISIBLE
            update_profile_education_edit_text.setText(currentlyLoggedUser.education)
        }
        if(currentlyLoggedUser.educationOrganization != null){
            update_profile_education_org_edit_text.visibility = View.VISIBLE
            update_profile_education_org_edit_text.setText(currentlyLoggedUser.educationOrganization)
        }
        if(currentlyLoggedUser.major != null){
            update_profile_major_edit_text.visibility = View.VISIBLE
            update_profile_major_edit_text.setText(currentlyLoggedUser.major)
        }
        if(currentlyLoggedUser.work != null){
            update_profile_work_edit_text.visibility = View.VISIBLE
            update_profile_work_edit_text.setText(currentlyLoggedUser.work)
        }
        if(currentlyLoggedUser.company != null){
            update_profile_company_edit_text.visibility = View.VISIBLE
            update_profile_company_edit_text.setText(currentlyLoggedUser.company)
        }

        checkUserImage()
    }

    private fun checkUserImage() {
        Log.d(TAG, "checkUserImage")
        update_profile_image_view.setImageResource(R.drawable.person_placeholder)
        if(currentlyLoggedUser.hasImage){
            Log.d(TAG, "has image")
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, Uri.parse(currentlyLoggedUser.imageFilePath))
            update_profile_image_view.setImageBitmap(bitmap)
        }
    }

    private fun setUpdateProfilePictureClickListener(){
        update_profile_update_image_text_view.setOnClickListener{
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT  //ACTION_OPEN_DOCUMENT instead of ACTION_GET_DOCUMENT to have future access to this file
            startActivityForResult(Intent.createChooser(intent, "Select Image"), Constants.PICK_IMAGE_REQUEST)
        }
    }
}