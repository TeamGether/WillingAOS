package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import com.google.firebase.database.*
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeInfo
import com.teamgether.willing.viewModel.ChallengeViewModel
import kotlinx.android.synthetic.main.activity_challenge_create.*


class ChallengeCreateActivity : ChallengeViewModel() {


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_create)

        nextpage.setOnClickListener {
            val challengeInfo = ChallengeInfo()

            challengeInfo.writeTime = ServerValue.TIMESTAMP

            val content = write_goal.text.toString()
            val reason =write_reason.text.toString()
            val tobe =write_wanttobe.text.toString()
            val friends =write_goalTogether.text.toString()



            setData(content,reason,tobe,friends)

            val intent = Intent(this,ChallengeCreateActivity2::class.java)
            startActivity(intent)


        }



    }

    private fun setData(content: String, reason: String, tobe: String, friends: String) {


    }


}






