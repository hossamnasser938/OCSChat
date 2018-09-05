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
import com.example.android.ocschat.model.Friend
import com.example.android.ocschat.model.User
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.util.Utils
import com.example.android.ocschat.viewModel.AddFriendViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_friend_info.*
import kotlinx.android.synthetic.main.fragment_user_info.*
import javax.inject.Inject

class FriendInfoFragment : Fragment() {

    @Inject
    lateinit var addFriendViewMdel : AddFriendViewModel
    private lateinit var transition : AddFriendFragment.AddFriendTransitionInterface

    private lateinit var isFriendDisposable : Disposable
    private lateinit var addFriendDisposable : Disposable

    private lateinit var currentUser: User

    companion object {
        fun newInstance(user : User) : FriendInfoFragment {
            val friendInfoFragment = FriendInfoFragment()
            val args = Bundle()
            args.putSerializable(Constants.USER_KEY, user)
            friendInfoFragment.arguments = args
            return friendInfoFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
        transition = activity as AddFriendFragment.AddFriendTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_friend_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUser = arguments?.get(Constants.USER_KEY) as User
        displayFriendInfo(currentUser)

        checkFriendImage(currentUser)

        if(!Utils.isNetworkConnected(context)){
            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
        }
        else{
            checkFriendshipState(currentUser)
        }
    }

    override fun onPause() {
        super.onPause()

        try { isFriendDisposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }

        try { addFriendDisposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }
    }

    /**
     * Check if this user is a friend of current user or not
     */
    private fun checkFriendshipState(currentUser : User){
        isFriendDisposable = addFriendViewMdel.isFriend(currentUser.id).subscribe({
            if(it){
                showFriendState()
            }
            else{
                showAddFriendState()
                setAddFriendButtonClickListen(currentUser)
            }
        }, {
            Toast.makeText(context, Constants.FAILED_CHECKING_FRIENDSHIPSTATE, Toast.LENGTH_SHORT).show()
            Log.d("FriendInfoFragment", it.message)
        })
    }

    private fun setAddFriendButtonClickListen(currentUser: User) {
        friend_info_add_friend_button.setOnClickListener {
            val friend = Friend(currentUser.id)
            addFriendDisposable = addFriendViewMdel.addFriend(friend).subscribe({
                showFriendState()
                Toast.makeText(context, R.string.friend_added, Toast.LENGTH_SHORT).show()
                activity?.finish()
            }, {
                Toast.makeText(context, R.string.error_adding_friend, Toast.LENGTH_SHORT).show()
                Log.d("FriendInfoFragment", it.message)
            })
        }
    }

    private fun checkFriendImage(currentUser: User){
        if(currentUser.hasImage){
            //TODO: Postponed functionality
        }
        else{ friend_info_image_view.setImageResource(R.drawable.person_placeholder)
        }
    }

    private fun displayFriendInfo(user : User){
        friend_info_name_text_view.text = user.firstName + " " + user.lastName
        if(user.age != null){
            friend_info_age_text_view.visibility = View.VISIBLE
            friend_info_age_text_view.setText(user.age)
        }
        if(user.education != null){
            friend_info_education_text_view.visibility = View.VISIBLE
            friend_info_education_text_view.setText(user.education)
        }
        if(user.educationOrganization != null){
            friend_info_education_org_text_view.visibility = View.VISIBLE
            friend_info_education_org_text_view.setText(user.educationOrganization)
        }
        if(user.major != null){
            friend_info_major_text_view.visibility = View.VISIBLE
            friend_info_major_text_view.setText(user.major)
        }
        if(user.work != null){
            friend_info_work_text_view.visibility = View.VISIBLE
            friend_info_work_text_view.setText(user.work)
        }
        if(user.company != null){
            friend_info_company_text_view.visibility = View.VISIBLE
            friend_info_company_text_view.setText(user.company)
        }

        checkFriendImage(user)
    }

    private fun showFriendState(){
        setMenuVisibility(false)
        friend_info_add_friend_button.visibility = View.VISIBLE
        friend_info_add_friend_button.background = resources.getDrawable(R.drawable.already_friend)
        friend_info_add_friend_button.setTextColor(resources.getColor(R.color.black))
        friend_info_add_friend_button.text = resources.getString(R.string.friend)
    }

    private fun showAddFriendState(){
        setMenuVisibility(false)
        friend_info_add_friend_button.visibility = View.VISIBLE
        friend_info_add_friend_button.background = resources.getDrawable(R.drawable.add_friend)
        friend_info_add_friend_button.setTextColor(resources.getColor(R.color.white))
        friend_info_add_friend_button.text = resources.getString(R.string.add_friend)
    }
}