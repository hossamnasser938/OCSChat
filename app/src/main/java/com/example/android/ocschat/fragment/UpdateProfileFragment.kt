package com.example.android.ocschat.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.ocschat.R
import com.example.android.ocschat.model.User
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.viewModel.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_update_profile.*
import javax.inject.Inject

class UpdateProfileFragment : Fragment(){

    @Inject
    lateinit var settingsViewModel: SettingsViewModel

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_update_profile , container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentlyLoggedUser = arguments?.get(Constants.USER_KEY) as User
        loadUserInfo()

    }

    private fun loadUserInfo(){
        try {
            update_profile_name_edit_text.setText(currentlyLoggedUser.name)
            //TODO: set user image
        }
        catch (e : UninitializedPropertyAccessException){ }
    }
}