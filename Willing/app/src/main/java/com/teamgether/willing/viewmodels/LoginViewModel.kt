package com.teamgether.willing.viewmodels

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
        //auth.languageCode = "ko"


    }

    fun login(
        email: String,
        password: String,
        alertUser: () -> Unit,
        alertEmail: () -> Unit,
        gotoMain: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("login", "성공")
                val user = auth.currentUser
                //getUserVerification(alertEmail, gotoMain)
                val nextIntent = Intent(this, MainActivity::class.java)
                startActivity(nextIntent)
                finish()
                Log.d("userVerificationin ", user?.isEmailVerified.toString())

            } else {
                alertUser()

            }
        }
    }


    //sentAlarm

    // email 인증 확인 후 intent
    fun getUserVerification(alertEmail: () -> Unit, gotoMain: () -> Unit) {
        val user = Firebase.auth.currentUser
        if (user != null) {
            if (user.isEmailVerified) {
                if (user != null) {
                    Log.d("userVerificationin ", user.isEmailVerified.toString())
                }
                //인텐트
                gotoMain()
    /*            val nextIntent = Intent(this, MainActivity::class.java)
                startActivity(nextIntent)
                finish()*/

            } else {
                alertEmail()
                Firebase.auth.signOut()
            }
        }

//유저의 db 값이 false인지 판단 후 false면 User에게 Verfication mail을 send하고 true면 메인화면으로 진입할 수 있도록 한다.

    }
}