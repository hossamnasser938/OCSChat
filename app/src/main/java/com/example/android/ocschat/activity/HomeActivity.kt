package com.example.android.ocschat.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.android.ocschat.R
import com.google.firebase.auth.FirebaseAuth

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        if(FirebaseAuth.getInstance().currentUser == null){
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId){
            R.id.sign_out_menu_item -> {
                //Sign user out and close app
                FirebaseAuth.getInstance().signOut()
                this.finishAffinity()
            }
        }
        return true
    }
}
