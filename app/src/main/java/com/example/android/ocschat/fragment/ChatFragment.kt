package com.example.android.ocschat.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.adapter.ChatAdapter
import com.example.android.ocschat.model.Message
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.viewModel.ChatViewModel
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_chat.*
import javax.inject.Inject

class ChatFragment : Fragment() {

    @Inject
    lateinit var chatViewModel: ChatViewModel
    private lateinit var disposable : Disposable
    private lateinit var chatAdapter: ChatAdapter

    private val messagesList = ArrayList<Message>()

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
        val friendId = arguments?.getString(Constants.FRIEND_ID_KEY)
        Log.d("ChatFragment", friendId)
        fetchMessages(friendId)
        displayMessages()
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

    private fun fetchMessages(friendId: String?){
        disposable = chatViewModel.getMessages(friendId)
                .subscribe({
                    messagesList.add(it)
                },{
                    Log.d("ChatFragment", it.message)
                })
    }

    private fun displayMessages(){
        val layoutManager = LinearLayoutManager(context)
        chat_recycler_view.layoutManager = layoutManager
        chat_recycler_view.setHasFixedSize(true)
        chat_recycler_view.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))

        chatAdapter = ChatAdapter(context, messagesList)
        chat_recycler_view.adapter = chatAdapter
    }

    interface ChatTransitionInterface{
        fun openFragment(fragment: Fragment)
    }

}