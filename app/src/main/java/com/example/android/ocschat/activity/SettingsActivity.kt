package com.example.android.ocschat.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.android.ocschat.R
import com.example.android.ocschat.fragment.SettingsFragment

class SettingsActivity : AppCompatActivity(), SettingsFragment.SettingsTransitionInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    override fun openFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_frame_layout, fragment)
                .commit()
    }
}
