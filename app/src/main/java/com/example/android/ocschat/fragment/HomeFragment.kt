package com.example.android.ocschat.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import android.widget.Toast
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.activity.AddFriendActivity
import com.example.android.ocschat.activity.ChatActivity
import com.example.android.ocschat.adapter.HomeAdapter
import com.example.android.ocschat.listener.HomeOnClickListener
import com.example.android.ocschat.model.User
import com.example.android.ocschat.model.UserState
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.util.Utils
import com.example.android.ocschat.viewModel.HomeViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_home.*
import org.eclipse.jdt.internal.core.util.Util
import javax.inject.Inject

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    @Inject
    lateinit var homeViewModel : HomeViewModel

    private lateinit var userState: UserState

    private lateinit var disposable: Disposable

    private val friendsList = ArrayList<User>()
    private lateinit var adapter : HomeAdapter

    companion object {
        fun newInstance(userState: UserState) : HomeFragment{
            val homeFragment = HomeFragment()
            val args = Bundle()
            args.putSerializable(Constants.USER_STATE_KEY, userState)
            homeFragment.arguments = args
            return homeFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate executes")
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated executes")
        super.onViewCreated(view, savedInstanceState)

        handleAddFriendButton()

        userState = arguments?.getSerializable(Constants.USER_STATE_KEY) as UserState
        Log.d(TAG, userState.name)
        when(userState){
            UserState.JUST_REGISTERED -> {
                showNewUserText()
                Log.d(TAG, "clear Download flag")
                Utils.clearDownloadFlag(context)
                Log.d(TAG, "needsDownload = " + Utils.isDownloadFlag(context))
            }
            UserState.JUST_LOGGED -> {
                //set download flag in shared preferences
                Log.d(TAG, "set Download flag")
                Utils.setDownloadFlag(context)
                Log.d(TAG, "needsDownload = " + Utils.isDownloadFlag(context))
                //download friends
                prepareforDisplayingFriends()
                fetchCurrentUserFriends(userState)
            }
            UserState.LOGGED_BEFORE -> {
                prepareforDisplayingFriends()

                Log.d(TAG, "check download flag")
                val downloadFlag = Utils.isDownloadFlag(context)
                Log.d(TAG, "Download flag = " + downloadFlag)
                if(downloadFlag){
                    fetchCurrentUserFriends(UserState.JUST_LOGGED)
                }
                else{
                    fetchCurrentUserFriends(userState)  //normal logged before
                }
            }
        }
    }

    override fun onPause() {
        Log.d(TAG, "onPause executes")
        super.onPause()

        try { disposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }
    }

    override fun onResume() {
        Log.d(TAG, "onResume executes")
        Log.d(TAG, "userState = " + userState.name)
        super.onResume()
        try {
            if (disposable.isDisposed) {
                Log.d(TAG, "disposed")
                //check if user has just logged but already downloaded friends
                if(userState == UserState.JUST_LOGGED){
                    Log.d(TAG, "check download flag")
                    val downloadFlag = Utils.isDownloadFlag(context)
                    Log.d(TAG, "Download flag = " + downloadFlag)
                    if(downloadFlag){
                        fetchCurrentUserFriends(userState)
                    }
                    else{
                        fetchCurrentUserFriends(UserState.LOGGED_BEFORE)  //normal logged before
                    }
                }
                else{
                    fetchCurrentUserFriends(userState)
                }
            }
        }
        catch (e : UninitializedPropertyAccessException){
            //if disposable has not been initialized before so check just registered user state
            try {
                if(userState == UserState.JUST_REGISTERED){
                    Log.d(TAG, "just registered")
                    //try to access adapter to see if it has been initialized or not
                    adapter.toString()
                    //if it has been initialized fetch friends
                    fetchCurrentUserFriends(userState)
                }
            }
            catch (e : UninitializedPropertyAccessException){
                //if the adapter has not been initialized initialize it
                Log.d(TAG, "adapter has not been initialized")
                prepareforDisplayingFriends()
            }
        }

    }

    fun handleAddFriendButton(){
        add_friend_button.setOnClickListener {
            val intent = Intent(activity, AddFriendActivity::class.java)
            startActivity(intent)
        }
    }

    fun prepareforDisplayingFriends(){
        val layoutManager = LinearLayoutManager(context)

        friends_recycler_view.layoutManager = layoutManager
        friends_recycler_view.setHasFixedSize(true)

        adapter = HomeAdapter(context, friendsList)
        friends_recycler_view.adapter = adapter

        friends_recycler_view
                .addOnItemTouchListener(HomeOnClickListener(context, friends_recycler_view,
                        HomeOnClickListener.ClickListener {
                            view, position ->
                            val clickedFriend = friendsList[position]
                            //Navigate to chat activity passing clicked friend
                            val intent = Intent(activity, ChatActivity::class.java)
                            intent.putExtra(Constants.FRIEND_ID_KEY, clickedFriend.id)
                            startActivity(intent)
                        }))
    }

    private fun fetchCurrentUserFriends(sentUserState: UserState) {
        //show loading progress bar
        loading_progress_bar.visibility = View.VISIBLE
        //hide failure list text
        failure_list_text_view.visibility = View.GONE

        disposable = homeViewModel.getCurrentUserFriends(sentUserState).subscribe({
            //hide loading progress bar
            loading_progress_bar.visibility = View.GONE

            if(!Utils.userExistsInList(friendsList, it)){
                Log.d(TAG, "got user : " + it.firstName + " and displayed")
                friendsList.add(it)
                adapter.notifyDataSetChanged()
            }
            else
                Log.d(TAG, "got user : " + it.firstName + " but not displayed")
        }, {
            Log.d(TAG, "Got throwable: " + it.message)
            Toast.makeText(context, Constants.FAILED_LOADING_FRIENDS, Toast.LENGTH_SHORT).show()
        }, {
            //onComplete
            Log.d(TAG, "received onComplete getUserFriends")
            //if loading progress bar is still visible hence user has no friends
            if(loading_progress_bar.visibility == View.VISIBLE){
                Log.d(TAG, "hide loading PB")
                loading_progress_bar.visibility = View.GONE
                when(sentUserState){
                    UserState.JUST_LOGGED, UserState.LOGGED_BEFORE -> {
                        Log.d(TAG, "show empty list text")
                        showEmptyListText()
                    }
                    UserState.JUST_REGISTERED -> {
                        Log.d(TAG, "show new user text")
                        showNewUserText()
                    }
                }
            }
        })
    }

    private fun showEmptyListText(){
        failure_list_text_view.visibility = View.VISIBLE
        failure_list_text_view.text = resources.getString(R.string.empty_friends_list)
    }

    private fun showNewUserText(){
        failure_list_text_view.visibility = View.VISIBLE
        failure_list_text_view.text = resources.getString(R.string.new_user_message)
    }

    interface HomeTransitionInterface{
        fun openFragment(fragment : Fragment)
    }
}