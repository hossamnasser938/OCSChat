package com.example.android.ocschat.fragment

import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.util.Log
import android.view.*
import com.example.android.ocschat.R
import com.example.android.ocschat.model.User
import com.example.android.ocschat.util.Constants
import kotlinx.android.synthetic.main.fragment_user_info.*
import java.io.IOException

class UserInfoFragment : Fragment() {

    private val TAG = "UserInfoFragment"

    private lateinit var transition : SettingsFragment.SettingsTransitionInterface

    private lateinit var currentUser: User

    companion object {
        fun newInstance(user : User) : UserInfoFragment {
            val userInfoFragment = UserInfoFragment()
            val args = Bundle()
            args.putSerializable(Constants.USER_KEY, user)
            userInfoFragment.arguments = args
            return userInfoFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transition = activity as SettingsFragment.SettingsTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set button image to update profile
        user_info_main_button.setImageDrawable(resources.getDrawable(R.mipmap.update_profile_24dp))

        currentUser = arguments?.get(Constants.USER_KEY) as User
        displayCurrentUserInfo(currentUser)

        setOnMainButtonClicked()
    }

    override fun onResume() {
        super.onResume()
        activity?.title = getString(R.string.my_profile)
    }

    private fun setOnMainButtonClicked(){
        user_info_main_button.setOnClickListener{
            //open update profile fragment
            transition.openFragment(UpdateProfileFragment.newInstance(currentUser))
        }
    }

    private fun checkUserImage(currentUser: User){
        Log.d(TAG, "checkUserImage")
        Log.d(TAG, "imageFilePath = " + currentUser.imageFilePath)
        if(currentUser.hasImage){
            Log.d(TAG, "has image")
            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, Uri.parse(currentUser.imageFilePath))
                val  drawable = RoundedBitmapDrawableFactory.create(resources, bitmap)
                drawable.isCircular = true
                user_info_image_view.setImageDrawable(drawable)
            }
            catch (e : IOException){
                e.printStackTrace()
            }
        }
        else{
            //TODO: convert into rounded
            user_info_image_view.setImageResource(R.drawable.person_placeholder)
        }
    }

    private fun displayCurrentUserInfo(user : User){
        user_info_name_text_view.text = user.name
        if(user.age != null){
            user_info_age_layout.visibility = View.VISIBLE
            user_info_age_text_view.text = user.age.toString()
        }
        if(user.education != null){
            user_info_education_layout.visibility = View.VISIBLE
            user_info_education_text_view.setText(user.education)
        }
        if(user.educationOrganization != null){
            user_info_education_org_layout.visibility = View.VISIBLE
            user_info_education_org_text_view.setText(user.educationOrganization)
        }
        if(user.major != null){
            user_info_major_layout.visibility = View.VISIBLE
            user_info_major_text_view.setText(user.major)
        }
        if(user.work != null){
            user_info_work_text_view.visibility = View.VISIBLE
            user_info_work_text_view.setText(user.work)
        }
        if(user.company != null){
            user_info_company_layout.visibility = View.VISIBLE
            user_info_company_text_view.setText(user.company)
        }

        checkUserImage(user)
    }

}