package com.teamgether.willing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.teamgether.willing.Fragment.UserProfileFragment
import com.teamgether.willing.fragments.*
import com.teamgether.willing.databinding.ActivityMainBinding
import com.teamgether.willing.fragments.FriendsFragment
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding


    private var userEmail: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //userEmail 전달 -> 프로필에서 본인 프로필인지 검사할 때 사용


        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this

        binding.mainBottomNavigation.setOnNavigationItemSelectedListener(this)

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)

        supportFragmentManager.beginTransaction().add(R.id.main_frameLayout, ChallengeFragment()).commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_challenge -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, ChallengeFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.menu_ranking -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, TrialFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.menu_feed -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, FeedFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.menu_friends -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout, FriendsFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.menu_mypage -> {
                userEmail = intent.getStringExtra("email").toString()

                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frameLayout,UserProfileFragment().apply {
                        arguments = Bundle().apply {
                            putString("userEmail", userEmail)
                        }
                    }).commitAllowingStateLoss()
                return true
            }
        }
        return false
    }

}