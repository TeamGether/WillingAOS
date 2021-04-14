package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.database.*
import com.google.firestore.v1.DocumentTransform
import com.teamgether.willing.MainActivity
import com.teamgether.willing.R
import com.teamgether.willing.databinding.ActivityMainBinding
import com.teamgether.willing.model.ChallengeInfo
import com.teamgether.willing.viewModel.ChallengeViewModel
import kotlinx.android.synthetic.main.activity_challenge_create2.*
import java.sql.Types.TIMESTAMP
import java.util.*

class ChallengeCreateActivity2 :  ChallengeViewModel(){
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_create2)



        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
      

        val money = write_money.text.toString()
        val set = write_set.text.toString()
        val detail = write_detail.text.toString()

        start.setOnClickListener {

            val intent = Intent(this,MainActivity::class.java)

            intent.putExtra("money",money)
            intent.putExtra("set",set)
            intent.putExtra("detail",detail)


            startActivity(intent)
        }



    }




}




