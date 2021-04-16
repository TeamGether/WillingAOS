package com.teamgether.willing.viewModel

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.teamgether.willing.MainActivity
import com.teamgether.willing.model.UserInfo
import com.teamgether.willing.view.LoginActivity
import com.google.firebase.auth.UserInfo as UserInfo1

open class LoginViewModel : AppCompatActivity() {

    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        auth.languageCode = "ko"


    }

    fun login(email: String, password: String, alertUser: () -> Unit, alertEmail: () -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("login", "성공")
                val user = auth.currentUser
                getUserVerification(alertEmail)

                Log.d("userVerificationin ", user.isEmailVerified.toString())

            } else {
                alertUser()
//                val builder = AlertDialog.Builder(this)
//                    .setTitle("로그인 실패")
//                    .setMessage("등록된 사용자가 아니거나 비밀번호가 틀렸습니다. 로그인 정보를 다시 확인해주세요!")
//                    .setPositiveButton("확인"){
//                            _: DialogInterface?, _: Int ->
//                    }
//                builder.show()
            }
        }
    }


    //sentAlarm

    // email 인증 확인 후 intent
    fun getUserVerification(alertEmail: () -> Unit) {
        val user = Firebase.auth.currentUser
        if (user.isEmailVerified) {
            Log.d("userVerificationin ", user.isEmailVerified.toString())
            //인텐트
            val nextIntent = Intent(this, MainActivity::class.java)
            startActivity(nextIntent)
        } else {
            //알럿
//            val builder = AlertDialog.Builder(this)
//                .setTitle("이메일 확인을 통해 메일 인증을 진행해주세요!")
//                .setMessage("입력하신 메일의 메일함에 들어가 인증 링크를 눌러주세요!")
//                .setPositiveButton("확인"){
//                        _: DialogInterface?, _: Int ->
//                }
//            builder.show()
            alertEmail()
            Firebase.auth.signOut()
        }


//유저의 db 값이 false인지 판단 후 false면 User에게 Verfication mail을 send하고 true면 메인화면으로 진입할 수 있도록 한다.

    }
}