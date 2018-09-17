package com.example.android.ocschat.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.*
import android.widget.Toast
import com.example.android.ocschat.OCSChatApplication
import com.example.android.ocschat.R
import com.example.android.ocschat.adapter.AddFriendAdapter
import com.example.android.ocschat.model.User
import com.example.android.ocschat.util.Utils
import com.example.android.ocschat.viewModel.AddFriendViewModel
import com.google.firebase.auth.FirebaseAuth
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_add_friend.*
import rx.Observer
import rx.Subscription
import javax.inject.Inject

class AddFriendFragment : Fragment() {

    private val TAG = "AddFriendFragment"

    @Inject
    lateinit var addFriendViewModel: AddFriendViewModel

    lateinit var transition : AddFriendTransitionInterface

    private lateinit var disposable : Disposable

    private val usersList = ArrayList<User>()
    private lateinit var adapter : AddFriendAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate executes")
        super.onCreate(savedInstanceState)
        (activity?.application as OCSChatApplication).component.inject(this)
        transition = activity as AddFriendTransitionInterface
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_add_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d(TAG, "onViewCreated")
        super.onViewCreated(view, savedInstanceState)
        setDropDownItemsClickListener()

        if(!Utils.isNetworkConnected(context)){
            Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
        }

        setAutoCompleteProperty()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.add_friend_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.invite_friend_menu_item -> {
                transition.openFragment(InviteFriendFragment())
            }
        }
        return true
    }

    override fun onPause() {
        Log.d(TAG, "onPause executes")
        super.onPause()

        try {
            Log.d(TAG, "dispose")
            disposable.dispose()
        }
        catch (e : UninitializedPropertyAccessException){ }
    }

    override fun onResume() {
        Log.d(TAG, "onResume executes")
        super.onResume()
        activity?.title = getString(R.string.add_friend)
        try {
            if(disposable.isDisposed){
                Log.d(TAG, "isDisposed")

                if(!Utils.isNetworkConnected(context)){
                    Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                }

                Log.d(TAG, "usersList size = " + usersList.size)
                fetchSuggestions()
            }
        }
        catch (e : UninitializedPropertyAccessException){
            //just resume
        }
    }

    /**
     * Set Auto complete property while search for friends
     */
    fun setAutoCompleteProperty(){
        adapter = AddFriendAdapter(context, usersList)
        add_friend_auto_complete.setAdapter(adapter)

        fetchSuggestions()
    }

    private fun fetchSuggestions() {
        val currentUserID = FirebaseAuth.getInstance().currentUser?.uid

        disposable = addFriendViewModel.suggestedUsers.subscribe({
            //Check if it is the currently logged user do not add it to the list
            if (!it.id.equals(currentUserID, false) && !Utils.userExistsInList(usersList, it)) {
                usersList.add(it)
                Log.d(TAG, "got friend : " + it.firstName + " and added")
                Log.d(TAG, it.id)
                Log.d(TAG, usersList.size.toString())
            }
            else{
                Log.d(TAG, "got friend : " + it.firstName + " but not added")
            }
        }, {
            Log.d(TAG, it.message)
        })
    }

    private fun setDropDownItemsClickListener() {
        add_friend_auto_complete.setOnItemClickListener { parent, view, position, id ->
            val clickedUser = adapter.getItem(position)
            transition.openFragment(FriendInfoFragment.newInstance(clickedUser))
        }
    }

    interface AddFriendTransitionInterface{
        fun openFragment(fragment: Fragment)
    }

}