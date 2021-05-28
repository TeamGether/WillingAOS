package com.teamgether.willing.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.google.firebase.firestore.FirebaseFirestore
import com.teamgether.willing.MainActivity
import com.teamgether.willing.R
import com.teamgether.willing.databinding.ActivityChallengeCheckBinding
import com.teamgether.willing.model.ChallengeInfo

class ChallengeCheckActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengeCheckBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_challenge_check)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_check)
        binding.check = this

        val challengeInfo = ChallengeInfo()
        challengeInfo.title = intent.getStringExtra("title")
        challengeInfo.account = intent.getIntExtra("account", 0)
        challengeInfo.bank = intent.getStringExtra("bank")
        challengeInfo.category = intent.getStringExtra("category")
        challengeInfo.count = intent.getIntExtra("count", 0)
        challengeInfo.money = intent.getIntExtra("money", 0)
        challengeInfo.per_week = intent.getIntExtra("per_week", 0)
        challengeInfo.percent = intent.getIntExtra("percent", 0)
        challengeInfo.show = intent.getBooleanExtra("show", false)
        challengeInfo.total_count = intent.getIntExtra("total_count", 0)
        challengeInfo.total_week = intent.getIntExtra("total_week", 0)

        binding.startBtn.setOnClickListener {
            db.collection("Challenge").add(challengeInfo)
                .addOnSuccessListener {
                    startActivity(Intent(this@ChallengeCheckActivity, MainActivity::class.java))
                    finish()
                }
                .addOnFailureListener {
                    Log.e("CheckActivity !!", it.message.toString())
                }
        }

        binding.noBtn.setOnClickListener {
            finish()
        }
    }
}