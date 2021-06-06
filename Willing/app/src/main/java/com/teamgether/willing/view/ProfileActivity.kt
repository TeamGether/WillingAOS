package com.teamgether.willing.view

import android.os.Bundle
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
            userEmail = " "
        }


        //DetailActivity에서 UserEmail 받아오는거
        supportFragmentManager.beginTransaction().replace(R.id.profile_fl, UserProfileFragment()).commit()
        //profile_fl에 fragment 세팅해둠

        //fragment에 UserEmail 넘겨주는 거
        userEmail = intent.getStringExtra("email").toString()
        val fragment = UserProfileFragment()
        setEmailAtProfile(fragment,userEmail)

    }
    private fun setEmailAtProfile(fragment: Fragment, email:String) {
        val bundle = Bundle()
        bundle.putString("userEmail",email)

        fragment.arguments = bundle
    }
}