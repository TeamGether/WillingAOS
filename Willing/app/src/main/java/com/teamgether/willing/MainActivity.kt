package com.teamgether.willing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.teamgether.willing.Fragment.UserProfileFragment
import com.teamgether.willing.fragments.*
import com.teamgether.willing.databinding.ActivityMainBinding
import com.teamgether.willing.fragments.FriendsFragment
import java.util.jar.Manifest

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this

        binding.mainBottomNavigation.setOnNavigationItemSelectedListener(this)

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),1)

        supportFragmentManager.beginTransaction().add(R.id.main_frameLayout, ChallengeFragment()).commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_challenge -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frameLayout, ChallengeFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.menu_ranking -> {
                supportFragmentManager.beginTransaction().replace(R.id.main_frameLayout, TrialFragment()).commitAllowingStateLoss()
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