package com.teamgether.willing.viewModel

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.teamgether.willing.MainActivity
import com.teamgether.willing.model.UserInfo
import kotlin.math.log

open class SignupViewModel : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    val db = Firebase.firestore


    fun createUser(email: String, password: String) {
        auth = Firebase.auth
        auth.languageCode = "ko"

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startToast("회원가입 성공")
                        val nextIntent = Intent(this, MainActivity::class.java)
                        startActivity(nextIntent)
                    } else {
                        startToast("회원가입 실패")
                    }
                }
                .addOnFailureListener {
                    it.printStackTrace()
                }
    }

    fun setData(nickname: String, email: String, donationName: String) {

        val userInfo = UserInfo(nickname, email, donationName)

        db.collection("User").add(userInfo).addOnSuccessListener {
            startToast("데이터 추가 성공")
        }.addOnFailureListener {
            startToast("데이터 추가 실패..")
        }
    }

    private fun startToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}