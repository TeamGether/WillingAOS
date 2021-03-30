package com.teamgether.willing

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_signup.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    var userInfo = UserInfo()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()



        gotoLogin.setOnClickListener {
            val email = signup_email.text.toString()
            val password = signup_pw.text.toString()

            createUser(email, password)
            setData()

        }
        signup_emailAuth.setOnClickListener{

        }


    }


    private fun createUser(email: String, password: String) {
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
                    makeText(this, "회??", Toast.LENGTH_SHORT).show()
                }
    }

    private fun setData() {
        val nickname = signup_nickName.text.toString()
        val email = signup_email.text.toString()
        val dona = signup_dona.text.toString()

        userInfo = UserInfo(nickname, email, dona)

        db.collection("User").add(userInfo) .addOnSuccessListener{
            makeText(this, "데이터 추가 성공", LENGTH_SHORT).show()
        }.addOnFailureListener {
            makeText(this, "데이터 추가 실패..", LENGTH_SHORT).show()
        }
    }
    

    private fun startToast(msg: String) {
        makeText(this, msg, LENGTH_SHORT).show()
    }
}

