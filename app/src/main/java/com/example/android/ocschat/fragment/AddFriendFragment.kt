package com.example.android.ocschat.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.adapter.AddFriendAdapter
import com.example.android.ocschat.model.User
import com.example.android.ocschat.viewModel.AddFriendViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_add_friend.*
import javax.inject.Inject

class AddFriendFragment : Fragment() {

    @Inject
    lateinit var addFriendViewModel: AddFriendViewModel
    lateinit var transition : AddFriendTransitionInterface

    private lateinit var disposable : Disposable

    private val usersList = ArrayList<User>()
    private lateinit var adapter : AddFriendAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
        transition = activity as AddFriendTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Set Auto complete property while search
        adapter = AddFriendAdapter(context, usersList)
        add_friend_auto_complete.setAdapter(adapter)
        disposable = addFriendViewModel.allUsers.subscribe({
            Log.d("AddFriendFragment", usersList.size.toString())
            usersList.add(it)
            Log.d("AddFriendFragment", it.name)
            adapter.notifyDataSetChanged()
            Log.d("AddFriendFragment", adapter.count.toString())
        }, {
            Log.d("AddFriendFragment", it.message)
        })
    }

    override fun onPause() {
        super.onPause()
        try {
            disposable.dispose()
        }
        catch (e : UninitializedPropertyAccessException){
            //Just stop
        }
    }

    interface AddFriendTransitionInterface{
        fun openFragment(fragment: Fragment)
    }

}