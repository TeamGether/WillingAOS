package com.teamgether.willing.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.teamgether.willing.Fragment.UserProfileFragment
import com.teamgether.willing.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private var userEmail: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        if (intent.hasExtra("userEmail")) {
            userEmail = intent.getStringExtra("userEmail").toString()
        }else{
            userEmail = ""
        }

        if(!userEmail.isNullOrEmpty()){
            Log.d("TAG", "profileActivity: $userEmail")
            supportFragmentManager.beginTransaction().replace(R.id.profile_fl, UserProfileFragment().apply {
                arguments =Bundle().apply { putString("userEmail",userEmail) }
            }).commit()
        }
    }
}