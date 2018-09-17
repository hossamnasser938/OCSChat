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
        activity?.actionBar?.title = getString(R.string.my_profile)
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
        if(user.age != null){
            user_info_age_text_view.visibility = View.VISIBLE
            user_info_age_text_view.setText(user.age.toString())
        }
        if(user.education != null){
            user_info_education_text_view.visibility = View.VISIBLE
            user_info_education_text_view.setText(user.education)
        }
        if(user.educationOrganization != null){
            user_info_education_org_text_view.visibility = View.VISIBLE
            user_info_education_org_text_view.setText(user.educationOrganization)
        }
        if(user.major != null){
            user_info_major_text_view.visibility = View.VISIBLE
            user_info_major_text_view.setText(user.major)
        }
        if(user.work != null){
            user_info_work_text_view.visibility = View.VISIBLE
            user_info_work_text_view.setText(user.work)
        }
        if(user.company != null){
            user_info_company_text_view.visibility = View.VISIBLE
            user_info_company_text_view.setText(user.company)
        }

        checkUserImage(user)
    }

}