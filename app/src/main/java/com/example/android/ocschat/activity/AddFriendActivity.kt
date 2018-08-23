package com.example.android.ocschat.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.android.ocschat.R
import com.example.android.ocschat.fragment.AddFriendFragment

class AddFriendActivity : AppCompatActivity() , AddFriendFragment.AddFriendTransitionInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friend)
        openFragment(AddFriendFragment())
    }

    override fun openFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.add_friend_frame_layout, fragment)
                .commit()
    }
}
