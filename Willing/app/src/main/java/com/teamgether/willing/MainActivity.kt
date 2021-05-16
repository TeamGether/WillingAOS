package com.teamgether.willing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.teamgether.willing.Fragment.*
import com.teamgether.willing.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this

        binding.mainBottomNavigation.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().add(R.id.main_frameLayout, ChallengeFragment()).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_challenge -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frameLayout, ChallengeFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.menu_ranking -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frameLayout, RankingFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.menu_feed -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frameLayout, FeedFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.menu_friends -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frameLayout, FriendsFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.menu_mypage -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frameLayout, UserProfileFragment()).commitAllowingStateLoss()
                return true
            }
        }
        return false
    }
}