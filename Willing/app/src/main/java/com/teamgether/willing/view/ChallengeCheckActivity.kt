package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.teamgether.willing.MainActivity
import com.teamgether.willing.R
import com.teamgether.willing.viewModel.ChallengeViewModel
import kotlinx.android.synthetic.main.activity_challenge_check.*
import kotlinx.android.synthetic.main.activity_challenge_create.*

class ChallengeCheckActivity : ChallengeViewModel(){

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_challenge_check)



        ck_title.text = intent.getStringExtra("title")
        ck_reason.text = intent.getStringExtra("reason")
        ck_tobe.text = intent.getStringExtra("tobe")
        ck_money.text = intent.getStringExtra("money")
        ck_count.text = intent.getStringExtra("set")
        ck_detail.text = intent.getStringExtra("detail")
        ck_donate.text = intent.getStringExtra("donate")


        ck_start_btn.setOnClickListener {

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }
}