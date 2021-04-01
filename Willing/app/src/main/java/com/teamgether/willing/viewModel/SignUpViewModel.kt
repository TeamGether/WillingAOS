package com.teamgether.willing.viewModel

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.teamgether.willing.model.UserInfo
import com.teamgether.willing.view.LoginActivity

open class SignUpViewModel : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        auth = Firebase.auth
        auth.languageCode = "ko"

    }

    fun createUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        startToast("회원가입 성공")
                        val nextIntent = Intent(this, LoginActivity::class.java)
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