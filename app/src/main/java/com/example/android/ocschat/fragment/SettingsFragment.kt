package com.example.android.ocschat.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.model.User
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.viewModel.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : Fragment() {

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

    private lateinit var currentlyLoggedUser : User

    private lateinit var transition : SettingsTransitionInterface

    private lateinit var disposable : Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
        transition = activity as SettingsTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        displayCurrentUserInfo()
    }

    override fun onStop() {
        super.onStop()

        try { disposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }
    }

    //TODO: override onStart

    /**
     * display currently logged user information
     */
    private fun displayCurrentUserInfo() {
        settings_logged_user_image.setImageResource(R.drawable.person_placeholder)

        val currentlyLoggedUserId = FirebaseAuth.getInstance().currentUser?.uid
        disposable = settingsViewModel.getUser(currentlyLoggedUserId).subscribe({
            currentlyLoggedUser = it
            settings_logged_user_name.text = currentlyLoggedUser.name
            //TODO: set user image
            setUserInfoOnClickListener(currentlyLoggedUser)
            //Log.d("SettingsFragment", it.firstName)
        }, {
            Log.d("SettingsFragment", it.message)
            Toast.makeText(context, Constants.ERROR_FETCHING_USER_INFO, Toast.LENGTH_SHORT).show()
        })
    }

    private fun setUserInfoOnClickListener(user : User){
        settings_logged_user_info.setOnClickListener {
            //open user info fragment to display currently logged user info
            transition.openFragment(UserInfoFragment.newInstance(user))
        }
    }

    interface SettingsTransitionInterface{
        fun openFragment(fragment: Fragment)
    }
}