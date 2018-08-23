package com.example.android.ocschat.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.*
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.activity.AddFriendActivity
import com.example.android.ocschat.adapter.HomeAdapter
import com.example.android.ocschat.listener.HomeOnClickListener
import com.example.android.ocschat.model.User
import com.example.android.ocschat.viewModel.HomeViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_home.*
import javax.inject.Inject

class HomeFragment : Fragment() {

    @Inject
    lateinit var homeViewModel : HomeViewModel
    private lateinit var disposable: Disposable
    private lateinit var homeAdapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleAddFriendButton()
        fetchCurrentUserFriends()
    }

    fun handleAddFriendButton(){
        add_friend_button.setOnClickListener {
            val intent = Intent(activity, AddFriendActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchCurrentUserFriends() {
        disposable = homeViewModel.currentUserFriends.subscribe({
            Log.d("HomeFragment", "Got friends list")
            displayCurrentUserFriends(it)
        }, {
            Log.d("HomeFragment", "Got throwable: " + it.message)
        })
    }

    fun displayCurrentUserFriends(friendsList : List<User>){
        val layoutManager = LinearLayoutManager(context)

        friends_recycler_view.layoutManager = layoutManager
        friends_recycler_view.setHasFixedSize(true)
        friends_recycler_view.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        homeAdapter = HomeAdapter(context, friendsList)
        friends_recycler_view.adapter = homeAdapter

        friends_recycler_view
                .addOnItemTouchListener(HomeOnClickListener(context, friends_recycler_view,
                        HomeOnClickListener.ClickListener {
            view, position ->
                            val clickedFriend = friendsList.get(position)
                            //Navigate to chat activity passing clicked friend
                        }))
    }

    interface HomeTransitionInterface{
        fun openFragment(fragment : Fragment)
    }
}