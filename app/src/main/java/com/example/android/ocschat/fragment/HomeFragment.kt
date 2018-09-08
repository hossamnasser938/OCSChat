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

    @Inject
    lateinit var homeViewModel : HomeViewModel

    private lateinit var disposable: Disposable
    private lateinit var homeAdapter: HomeAdapter

    private val friendsList = ArrayList<User>()
    private lateinit var adapter : HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareforDisplayingFriends()

        //Fetch friends from repository
        fetchCurrentUserFriends()

        handleAddFriendButton()
    }

    override fun onPause() {
        super.onPause()
        try {
            disposable.dispose()
        }
        catch (e : UninitializedPropertyAccessException){
            //just stop
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            if(disposable.isDisposed){
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

    private fun fetchCurrentUserFriends() {
        disposable = homeViewModel.currentUserFriends.subscribe({
            Log.d("HomeFragment", "Got friend: " + it.firstName)
            failure_list_text_view.visibility = View.GONE
            friendsList.add(it)
        }, {
            Log.d("HomeFragment", "Got throwable: " + it.message)
            Toast.makeText(context, Constants.FAILED_LOADING_FRIENDS, Toast.LENGTH_SHORT).show()
        })
    }

    fun prepareforDisplayingFriends(){
        showEmptyListText()

        val layoutManager = LinearLayoutManager(context)

        friends_recycler_view.layoutManager = layoutManager
        friends_recycler_view.setHasFixedSize(true)

        homeAdapter = HomeAdapter(context, friendsList)
        friends_recycler_view.adapter = homeAdapter

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

    private fun showEmptyListText(){
        failure_list_text_view.visibility = View.VISIBLE
        failure_list_text_view.text = resources.getString(R.string.empty_friends_list)
    }

    interface HomeTransitionInterface{
        fun openFragment(fragment : Fragment)
    }
}