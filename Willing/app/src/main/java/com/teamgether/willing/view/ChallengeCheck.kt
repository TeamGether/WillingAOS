package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.teamgether.willing.MainActivity
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeInfo
import kotlinx.android.synthetic.main.activity_challenge_check.*
import kotlinx.android.synthetic.main.activity_challenge_create.*


class ChallengeCheck : AppCompatActivity() {

    private val TAG = "ChallengeCheck"
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_check)

//
//        val title = intent.getStringExtra("title")
//        val category = intent.getStringExtra("category")
//        val period = intent.getStringExtra("period")
//        val count = intent.getStringExtra("count")
//        val money = intent.getStringExtra("money")
//        val bank = intent.getStringExtra("bank")
//        val account = intent.getStringExtra("account")
//        val expose = intent.getStringExtra("expose")

        val data = hashMapOf(
            "title" to intent.getStringExtra("title"),
            "category" to intent.getStringExtra("category"),
            "period" to intent.getStringExtra("period"),
            "count" to intent.getStringExtra("count"),
            "money" to intent.getStringExtra("money"),
            "bank" to intent.getStringExtra("bank"),
            "account" to intent.getStringExtra("account"),
            "expose" to intent.getStringExtra("expose")


        )


        start_btn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)


            db.collection("Challenge")
                .add(data)
                .addOnSuccessListener{ documentReference ->
                    Log.d("challenge1", "add")
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }




        }
    }
}
