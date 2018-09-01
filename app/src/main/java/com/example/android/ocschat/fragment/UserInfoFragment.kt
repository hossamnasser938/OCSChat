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
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_user_info.*
import javax.inject.Inject

class UserInfoFragment : Fragment() {

    @Inject
    lateinit var addFriendViewMdel : AddFriendViewModel
    private lateinit var transition : AddFriendFragment.AddFriendTransitionInterface

    private lateinit var isFriendDisposable : Disposable
    private lateinit var addFriendDisposable : Disposable

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
        (activity?.application as OCSChatApplication).component.inject(this)
        transition = activity as AddFriendFragment.AddFriendTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentUser = arguments?.get(Constants.USER_KEY) as User
        displayCurrentUserInfo(currentUser)

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

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.user_info_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.update_profile_menu_item -> {
                //open update profile fragment
                try {
                    transition.openFragment(UpdateProfileFragment.newInstance(currentUser))
                }
                catch (e : UninitializedPropertyAccessException){ }

            }
        }
        return true
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
            Log.d("UserInfoFragment", it.message)
        })
    }

    private fun setAddFriendButtonClickListen(currentUser: User) {
        user_info_add_friend_button.setOnClickListener {
            val friend = Friend(currentUser.id)
            addFriendDisposable = addFriendViewMdel.addFriend(friend).subscribe({
                showFriendState()
                Toast.makeText(context, R.string.friend_added, Toast.LENGTH_SHORT).show()
                activity?.finish()
            }, {
                Toast.makeText(context, R.string.error_adding_friend, Toast.LENGTH_SHORT).show()
                Log.d("UserInfoFragment", it.message)
            })
        }
    }

    private fun checkFriendImage(currentUser: User){
        if(currentUser.hasImage){
            //Postponed functionality
        }
        else{
            user_info_image_view.setImageResource(R.drawable.person_placeholder)
        }
    }

    private fun displayCurrentUserInfo(user : User){
        user_info_name_text_view.text = user.name
        //TODO: set user image
    }

    private fun showFriendState(){
        setMenuVisibility(false)
        user_info_add_friend_button.visibility = View.VISIBLE
        user_info_add_friend_button.background = resources.getDrawable(R.drawable.already_friend)
        user_info_add_friend_button.setTextColor(resources.getColor(R.color.black))
        user_info_add_friend_button.text = resources.getString(R.string.friend)
    }

    private fun showAddFriendState(){
        setMenuVisibility(false)
        user_info_add_friend_button.visibility = View.VISIBLE
        user_info_add_friend_button.background = resources.getDrawable(R.drawable.add_friend)
        user_info_add_friend_button.setTextColor(resources.getColor(R.color.white))
        user_info_add_friend_button.text = resources.getString(R.string.add_friend)
    }

    private fun showUpdateProfileState(){
        setMenuVisibility(true)
    }

}