package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.teamgether.willing.R
import com.teamgether.willing.viewModel.ChallengeViewModel
import kotlinx.android.synthetic.main.activity_group_choice.*
import kotlinx.android.synthetic.main.activity_signup.*

class GroupChoiceActivity : ChallengeViewModel() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_choice)

        nofriends_btn.setOnClickListener {
            val intent = Intent(this,ChallengeCreateActivity::class.java)
            intent.putExtra("isGroup", "no")
            startActivity(intent)
        }

        withfriend_btn.setOnClickListener {
            val intent = Intent(this,ChallengeCreateActivity::class.java)
            intent.putExtra("isGroup","yes")
            startActivity(intent)
        }

    }
}