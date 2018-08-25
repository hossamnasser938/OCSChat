package com.example.android.ocschat.fragment

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.adapter.ChatAdapter
import com.example.android.ocschat.model.Message
import com.example.android.ocschat.model.User
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.util.Utils
import com.example.android.ocschat.viewModel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_chat.*
import javax.inject.Inject

class ChatFragment : Fragment() {

    @Inject
    lateinit var chatViewModel: ChatViewModel

    private lateinit var fetchMessgaesDisposable : Disposable
    private lateinit var pushMessagesDisposable: Disposable
    private lateinit var fetchUserDisposable: Disposable
    private lateinit var fetchFriendDisposable: Disposable

    private lateinit var chatAdapter: ChatAdapter

    private lateinit var currentUser : User
    private lateinit var friendUser : User

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
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        handleUserInput(friendId)
        if(!Utils.isNetworkConnected(context)){
            showNoInternetText()
        }
        else{
            fetchUsers(currentUserId, friendId)
            fetchMessages(friendId)
        }
    }

    override fun onPause() {
        super.onPause()

        try { fetchMessgaesDisposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }

        try { pushMessagesDisposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }

        try { fetchUserDisposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }

        try { fetchFriendDisposable.dispose() }
        catch (e : UninitializedPropertyAccessException){ }
    }

    private fun InitializeAdapter(){
        val layoutManager = LinearLayoutManager(context)
        layoutManager.stackFromEnd = true
        chat_recycler_view.layoutManager = layoutManager
        chat_recycler_view.setHasFixedSize(true)

        Log.d("ChatFragment", "Initiate adapter")
        chatAdapter = ChatAdapter(context, messagesList, currentUser.name)
        chat_recycler_view.adapter = chatAdapter
    }

    private fun fetchMessages(friendId: String?){
        fetchMessgaesDisposable = chatViewModel.getMessages(friendId)
                .subscribe({
                    messagesList.add(it)
                    chatAdapter.notifyDataSetChanged()
                    chat_recycler_view.scrollToPosition(chatAdapter.itemCount - 1)
                    Log.d("ChatFragment", it.text)
                },{
                    Log.d("ChatFragment", it.message)
                })
    }

    private fun fetchUsers(currentUserId : String?, friendId : String?){
        fetchUserDisposable = chatViewModel.getUser(currentUserId).subscribe({
            currentUser = it
            InitializeAdapter()
            Log.d("ChatFragment", it.name)
        }, {
            Log.d("ChatFragment", it.message)
            Toast.makeText(context, Constants.ERROR, Toast.LENGTH_SHORT).show()
            activity?.finish()
        })
        fetchFriendDisposable = chatViewModel.getUser(friendId).subscribe({
            friendUser = it
            Log.d("ChatFragment", it.name)
        }, {
            Log.d("ChatFragment", it.message)
            Toast.makeText(context, Constants.ERROR, Toast.LENGTH_SHORT).show()
            activity?.finish()
        })
    }

    private fun handleUserInput(friendId: String?) {
        send_fab.setOnClickListener {
            if(!Utils.isNetworkConnected(context)){
                Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
            }
            else{
                val messageText = message_text_input.text.toString()
                if(!messageText.isEmpty()){
                    val message = Message(messageText, currentUser.name, friendUser.name)
                    pushMessagesDisposable = chatViewModel.pushMessage(friendId, message)
                            .subscribe({
                                message_text_input.text.clear()
                                Log.d("ChatFragment", "Message sent")
                            }, {
                                Toast.makeText(context, Constants.FAILED_SENDING_MESSAGE, Toast.LENGTH_SHORT).show()
                                Log.d("ChatFragment", it.message)
                            })
                }
            }
        }
    }

    private fun showNoInternetText(){
        failure_chat_view.visibility = View.VISIBLE
        failure_chat_view.text = resources.getString(R.string.no_internet_connection)
    }

    interface ChatTransitionInterface{
        fun openFragment(fragment: Fragment)
    }

}