package com.teamgether.willing.viewModel

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.teamgether.willing.model.ChallengeInfo

open class ChallengeViewModel : AppCompatActivity(){

    lateinit var db: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore



        fun setData(content: String, reason:String, tobe:String, friends:String) {
            val challengeInfo = ChallengeInfo(content, reason, tobe,friends)

            db.collection("Challenge").add(challengeInfo).addOnSuccessListener {
                startToast("데이터 추가 성공")
            }.addOnFailureListener {
                startToast("데이터 추가 실패..")
            }
        }

        fun setData2(money:String, goal_number:String, period:String, donation:String) {
            val challengeInfo = ChallengeInfo(money, goal_number, period,donation)

            db.collection("Challenge").add(challengeInfo).addOnSuccessListener {
                startToast("데이터 추가 성공")
            }.addOnFailureListener {
                startToast("데이터 추가 실패..")
            }
        }


    }

    private fun startToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}