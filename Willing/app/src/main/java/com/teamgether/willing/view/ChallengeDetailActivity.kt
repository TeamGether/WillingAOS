package com.teamgether.willing.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeList
import kotlinx.android.synthetic.main.activity_challenge_detail.*

class ChallengeDetailActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_detail)

        challengeId.text = intent.getStringExtra("challengeId")
    }
}