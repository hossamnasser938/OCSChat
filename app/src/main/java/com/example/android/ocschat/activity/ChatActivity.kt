package com.example.android.ocschat.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.android.ocschat.R
import com.example.android.ocschat.fragment.ChatFragment

class ChatActivity : AppCompatActivity(), ChatFragment.ChatTransitionInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        openFragment(ChatFragment())
    }

    override fun openFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.login_frame_layout, fragment)
                .commit()
    }
}
