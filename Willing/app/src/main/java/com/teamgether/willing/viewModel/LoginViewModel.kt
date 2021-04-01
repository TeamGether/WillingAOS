package com.teamgether.willing.viewModel

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.teamgether.willing.MainActivity

open class LoginViewModel : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        auth.languageCode = "ko"

    }

    fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("login", "성공")
                val nextIntent = Intent(this, MainActivity::class.java)
                startActivity(nextIntent)

            } else {
                Log.d("login", "실패")
            }
        }
    }
    fun getUserVerification(){
//유저의 db 값이 false인지 판단 후 false면 User에게 Verfication mail을 send하고 true면 메인화면으로 진입할 수 있도록 한다.

    }
}