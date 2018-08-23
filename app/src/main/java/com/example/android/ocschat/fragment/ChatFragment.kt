package com.example.android.ocschat.fragment

import android.nfc.Tag
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.viewModel.ChatViewModel
import javax.inject.Inject

class ChatFragment : Fragment() {

    @Inject
    lateinit var chatViewModel: ChatViewModel

    companion object {
        fun newInstance(friendId : String) : ChatFragment{
            val chatFragment = ChatFragment()
            val args = Bundle()
            args.putString(Constants.FRIEND_ID_KEY, friendId)
            chatFragment.arguments = args
            return chatFragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(tag, arguments?.getString(Constants.FRIEND_ID_KEY))
    }

    interface ChatTransitionInterface{
        fun openFragment(fragment: Fragment)
    }

}