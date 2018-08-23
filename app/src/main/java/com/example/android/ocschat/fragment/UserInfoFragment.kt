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
import com.example.android.ocschat.viewModel.AddFriendViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_user_info.*
import javax.inject.Inject

class UserInfoFragment : Fragment() {

    @Inject
    lateinit var addFriendViewMdel : AddFriendViewModel

    private lateinit var isFriendDisposable : Disposable
    private lateinit var addFriendDisposable : Disposable

    private lateinit var user: User

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentUser = arguments?.get(Constants.USER_KEY) as User
        user_info_name_text_view.text = currentUser.name

        checkFriendshipState(currentUser)
    }

    override fun onPause() {
        super.onPause()

        try {
            isFriendDisposable.dispose()
        }
        catch (e : UninitializedPropertyAccessException){
            //Just stop
        }

        try {
            addFriendDisposable.dispose()
        }
        catch (e : UninitializedPropertyAccessException){
            //Just stop
        }
    }

    /**
     * Check if this user is a friend of current user or not
     */
    private fun checkFriendshipState(currentUser : User){
        isFriendDisposable = addFriendViewMdel.isFriend(currentUser.id).subscribe({
            if(it){
                user_info_friendship_state_text_view.visibility = View.VISIBLE
            }
            else{
                user_info_add_friend_button.visibility = View.VISIBLE
                setAddFriendButtonClickListen(currentUser)
            }
        }, {
            Log.d("UserInfoFragment", it.message)
        })
    }

    private fun setAddFriendButtonClickListen(currentUser: User) {
        user_info_add_friend_button.setOnClickListener {
            val friend = Friend(currentUser.id)
            addFriendDisposable = addFriendViewMdel.addFriend(friend).subscribe({
                Toast.makeText(context, R.string.friend_added, Toast.LENGTH_SHORT).show()
                user_info_add_friend_button.visibility = View.GONE
                user_info_friendship_state_text_view.visibility = View.VISIBLE
            }, {
                Toast.makeText(context, R.string.error_adding_friend, Toast.LENGTH_SHORT).show()
                Log.d("UserInfoFragment", it.message)
            })
        }
    }

}