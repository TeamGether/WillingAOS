package com.teamgether.willing.viewmodels

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.teamgether.willing.model.UserInfo
import kotlinx.android.synthetic.main.activity_signup.*

open class SignUpViewModel : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        auth = Firebase.auth
        //auth.languageCode = "ko"

    }

    fun createUser(email: String, password: String,name: String,donationName: String,startActivity:()->Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startToast("회원가입 성공")
                    setData(name, email, donationName,startActivity)

                } else {
                    startToast("회원가입 실패")
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                Log.d("result", "실패~")
            }
    }

    private fun setData(name: String, email: String, donationName: String,startActivity:()->Unit) {
        val userInfo = UserInfo(name, email, donationName)

        db.collection("User").add(userInfo).addOnSuccessListener {
            startToast("데이터 추가 성공")
            sendEmail(email,startActivity)

        }.addOnFailureListener {
            startToast("데이터 추가 실패..")
        }
    }

    fun nickNameCheck(name: String,  setDuplicate: (Boolean) -> Unit) {
        sign_up_warning_nickName.text = ""


        db.collection("User").whereEqualTo("name", name)
            .get()
            .addOnSuccessListener {
                setDuplicate(true)
            }
    }


    private fun sendEmail(email: String,startActivity:()->Unit){
        val user = Firebase.auth.currentUser

        user!!.sendEmailVerification()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    val userInfo = UserInfo()
                    Log.d("emailSend", " 성공")
                    Log.d("result", user.isEmailVerified.toString())
                    startActivity()

               }
            }
    }


    private fun startToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}