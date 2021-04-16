package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import com.google.firestore.v1.DocumentTransform
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeInfo
import com.teamgether.willing.viewModel.ChallengeViewModel
import kotlinx.android.synthetic.main.activity_challenge_create.*
import kotlinx.android.synthetic.main.activity_challenge_create2.*
import java.sql.Types.TIMESTAMP


class ChallengeCreateActivity : ChallengeViewModel() {


    override fun onCreate(savedInstanceState: Bundle?)  {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_create)


        if (intent.hasExtra("isGroup")) {
            val group : String? = intent.getStringExtra("isGroup")
            if (group == "no")
            {
                write_goalTogether_tv.visibility = View.INVISIBLE
                write_goalTogether.visibility = View.INVISIBLE
            }
            else if(group == "yes") {
                write_goalTogether_tv.visibility= View.VISIBLE
                write_goalTogether.visibility = View.VISIBLE
            }
        } else {
            Toast.makeText(this, "전달된 이름이 없습니다", Toast.LENGTH_SHORT).show()
        }

        val title : String? = write_goal.text.toString()
        val reason : String? = write_reason.text.toString()
        val tobe : String?= write_tobe.text.toString()

        nextpage.setOnClickListener {
            val intent = Intent(this,ChallengeCreateActivity2::class.java)
            intent.putExtra("title",title)
            intent.putExtra("reason",reason)
            intent.putExtra("tobe",tobe)

            startActivity(intent)
        }

    }




}





