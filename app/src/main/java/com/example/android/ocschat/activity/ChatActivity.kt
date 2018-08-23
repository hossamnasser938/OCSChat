package com.example.android.ocschat.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.android.ocschat.R
import com.example.android.ocschat.fragment.ChatFragment
import com.example.android.ocschat.util.Constants

class ChatActivity : AppCompatActivity(), ChatFragment.ChatTransitionInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        val friendId = intent.extras[Constants.FRIEND_ID_KEY] as String
        openFragment(ChatFragment.newInstance(friendId))
    }

    override fun openFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.chat_frame_layout, fragment)
                .commit()
    }
}
