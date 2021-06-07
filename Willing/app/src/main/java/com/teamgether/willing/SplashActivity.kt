package com.teamgether.willing

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.teamgether.willing.view.LoginActivity

class SplashActivity : AppCompatActivity() {
    private var auth = FirebaseAuth.getInstance()
    private var currentUser = auth.currentUser
    var userEmail: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart() {
        super.onStart()
        if (currentUser != null) {
            userEmail = currentUser?.email.toString()
            moveToMain(userEmail)
        } else {
            moveToLogin()
        }

    }

    private fun moveToMain(userEmail:String) {
        var intent = Intent(this, MainActivity::class.java)
        intent.putExtra("email", userEmail)
        startActivity(intent)
        finish()
    }

    private fun moveToLogin() {
        var intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

}