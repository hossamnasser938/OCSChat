package com.example.android.ocschat.fragment

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import android.widget.Toast
import com.example.android.ocschat.R
import com.example.android.ocschat.util.Constants
import com.example.android.ocschat.util.Utils
import kotlinx.android.synthetic.main.fragment_invite_friend.*

class InviteFriendFragment : Fragment() {

    private val TAG = "InviteFriendFragment"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.title = getString(R.string.invite_friend)
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
            R.id.invite_menu_item -> {
                //hide error text
                invite_friend_error_text_view.visibility = View.GONE

                //check empty email field
                if(checkEmptyEmail()){
                    showErrorText(R.string.enter_email)
                    return true
                }

                //check email validity format
                if(!Utils.isValidEmail(invite_friend_email_edit_text.text.toString())){
                    showErrorText(R.string.invalid_email)
                    return true
                }

                //create intent for sending email inviting friends
                val emailUriString = "mailto:" + invite_friend_email_edit_text.text.toString() + "?cc=" + Constants.EMAIL_SENDER + "&subject=" + Uri.encode(Constants.EMAIL_SUBJECT) + "&body=" + Uri.encode(Constants.EMAIL_BODY)
                val emailIntent = Intent()
                emailIntent.action = Intent.ACTION_SENDTO
                emailIntent.data = Uri.parse(emailUriString)
                try {
                    startActivity(emailIntent)
                }
                catch (e : ActivityNotFoundException){
                    Toast.makeText(context, R.string.opening_email_app_error, Toast.LENGTH_SHORT).show()
                }
            }
        }
        return true
    }

    private fun checkEmptyEmail() : Boolean{
        if(invite_friend_email_edit_text.text.toString().isEmpty())
            return true
        return false
    }

    private fun showErrorText(stringID : Int){
        invite_friend_error_text_view.visibility = View.VISIBLE
        invite_friend_error_text_view.text = resources.getString(stringID)
    }
}