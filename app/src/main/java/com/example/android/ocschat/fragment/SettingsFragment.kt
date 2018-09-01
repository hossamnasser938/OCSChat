package com.example.android.ocschat.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.ocschat.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {

    lateinit var transition : SettingsTransitionInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        transition = activity as SettingsTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayCurrentUserInfo()

    }

    /**
     * display currently logged user information
     */
    private fun displayCurrentUserInfo() {
        val currentUserName = FirebaseAuth.getInstance().currentUser?.displayName
        //TODO: set user name
        //settings_logged_user_name.text = currentUserName
        //TODO: set user image
    }

    private fun setUserInfoOnClickListener(){
        settings_logged_user_info.setOnClickListener {
            //open user info fragment to display currently logged user info
            transition.openFragment(UserInfoFragment())
        }
    }

    interface SettingsTransitionInterface{
        fun openFragment(fragment: Fragment)
    }
}