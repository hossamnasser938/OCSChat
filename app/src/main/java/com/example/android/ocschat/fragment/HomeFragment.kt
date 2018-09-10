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
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.util.Utils
import com.example.android.ocschat.viewModel.HomeViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    @Inject
    lateinit var homeViewModel : HomeViewModel

    private lateinit var disposable: Disposable

    private val friendsList = ArrayList<User>()
    private lateinit var adapter : HomeAdapter

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

        prepareforDisplayingFriends()

        //Fetch friends from repository
        fetchCurrentUserFriends()

        handleAddFriendButton()
    }

    override fun onPause() {
        Log.d(TAG, "onPause executes")
        super.onPause()

        try { disposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }
    }

    override fun onResume() {
        Log.d(TAG, "onResume executes")
        super.onResume()
        try {
            if(disposable.isDisposed) {
                Log.d(TAG, "disposed")
                adapter.clear()
                fetchCurrentUserFriends()
            }
        }
        catch (e : UninitializedPropertyAccessException){
            //just resume
        }
    }

    fun handleAddFriendButton(){
        add_friend_button.setOnClickListener {
            val intent = Intent(activity, AddFriendActivity::class.java)
            startActivity(intent)
        }
    }

    fun prepareforDisplayingFriends(){
        showEmptyListText()

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

    private fun fetchCurrentUserFriends() {
        disposable = homeViewModel.currentUserFriends.subscribe({
            Log.d(TAG, "Got friend at home: " + it.firstName)
            failure_list_text_view.visibility = View.GONE
            friendsList.add(it)
            adapter.notifyDataSetChanged()
        }, {
            Log.d(TAG, "Got throwable: " + it.message)
            Toast.makeText(context, Constants.FAILED_LOADING_FRIENDS, Toast.LENGTH_SHORT).show()
        })
    }

    private fun showEmptyListText(){
        failure_list_text_view.visibility = View.VISIBLE
        failure_list_text_view.text = resources.getString(R.string.empty_friends_list)
    }

    interface HomeTransitionInterface{
        fun openFragment(fragment : Fragment)
    }
}