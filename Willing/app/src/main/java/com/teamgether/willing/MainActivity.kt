package com.teamgether.willing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.teamgether.willing.Fragment.*
import com.teamgether.willing.databinding.ActivityMainBinding
import com.teamgether.willing.view.GroupChoiceActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding

    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.main = this

        binding.mainBottomNavigation.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction().add(R.id.main_frameLayout, ChallengeFragment()).commit()

        button2.setOnClickListener {
            val intent = Intent(this, GroupChoiceActivity::class.java)
            startActivity(intent)
        }


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
                supportFragmentManager.beginTransaction().replace(R.id.main_frameLayout, MyPageFragment()).commitAllowingStateLoss()
                return true
            }
        }
        return false
    }
}