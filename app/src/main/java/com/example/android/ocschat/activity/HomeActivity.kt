package com.example.android.ocschat.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import com.example.android.ocschat.R
import com.example.android.ocschat.fragment.HomeFragment
import com.example.android.ocschat.model.UserState
import com.example.android.ocschat.util.Constants
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity(), HomeFragment.HomeTransitionInterface {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        supportActionBar?.title = getString(R.string.home)
        if(FirebaseAuth.getInstance().currentUser == null){
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
        else{
            if(intent.hasExtra(Constants.USER_STATE_KEY)){
                val userState = intent.extras[Constants.USER_STATE_KEY] as UserState
                openFragment(HomeFragment.newInstance(userState))
            }
            else{
                openFragment(HomeFragment.newInstance(UserState.LOGGED_BEFORE))
            }

        }

    }

    override fun openFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .replace(R.id.home_frame_layout, fragment)
                .commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.sign_out_menu_item -> {
                //Sign user out and go to LoginActivity
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, R.string.signed_out, Toast.LENGTH_SHORT).show()
            }
            R.id.settings_menu_item -> {
                //open settings activity
                val intent = Intent(applicationContext, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

    override fun onBackPressed() {
        //Nothing
    }
}
