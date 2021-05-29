package com.teamgether.willing.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
        challengeInfo.UID = intent.getStringExtra("UID")
        challengeInfo.cntPerWeek = intent.getIntExtra("cntPerWeek", 0)
        challengeInfo.didFinish = intent.getBooleanExtra("didFinish", false)
        challengeInfo.didSuccess = intent.getBooleanExtra("didSuccess", false)
        challengeInfo.percent = intent.getLongExtra("percent", 0)
        challengeInfo.price = intent.getIntExtra("price", 0)
        challengeInfo.show = intent.getBooleanExtra("show", false)
        challengeInfo.subject = intent.getStringExtra("subject")
        challengeInfo.targetAccount = intent.getStringExtra("targetAccount")
        challengeInfo.targetBank = intent.getStringExtra("targetBank")
        challengeInfo.term = intent.getIntExtra("term", 0)
        challengeInfo.title = intent.getStringExtra("title")

        binding.startBtn.setOnClickListener {
            db.collection("Challenge").add(challengeInfo)
                .addOnSuccessListener {
                    Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show()
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