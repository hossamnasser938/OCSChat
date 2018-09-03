package com.example.android.ocschat.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import com.example.android.ocschat.R
import kotlinx.android.synthetic.main.activity_add_friend.view.*
import kotlinx.android.synthetic.main.fragment_invite_friend.*

class InviteFriendFragment : Fragment() {

    private val TAG = "InviteFriendFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_invite_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.invite_friend_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.invite_friend_menu_item -> {
                //TODO: invite friend
                Toast.makeText(context, invite_friend_email_edit_text.text.toString(), Toast.LENGTH_SHORT).show()
            }
        }
        return true
    }
}