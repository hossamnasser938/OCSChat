package com.example.android.ocschat.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.model.Friend
import com.example.android.ocschat.model.User
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.util.Utils
import com.example.android.ocschat.viewModel.AddFriendViewModel
import com.example.android.ocschat.viewModel.SettingsViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_user_info.*
import javax.inject.Inject

class UserInfoFragment : Fragment() {

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
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUser = arguments?.get(Constants.USER_KEY) as User
        displayCurrentUserInfo(currentUser)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.user_info_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.update_profile_menu_item -> {
                //open update profile fragment
                transition.openFragment(UpdateProfileFragment.newInstance(currentUser))
            }
        }
        return true
    }

    private fun checkUserImage(currentUser: User){
        if(currentUser.hasImage){
            //TODO: Postponed functionality
        }
        else{
            user_info_image_view.setImageResource(R.drawable.person_placeholder)
        }
    }

    private fun displayCurrentUserInfo(user : User){
        user_info_name_text_view.text = user.name
        checkUserImage(user)
    }

}