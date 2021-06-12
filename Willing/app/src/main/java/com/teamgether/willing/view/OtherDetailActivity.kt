package com.teamgether.willing.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.teamgether.willing.adapters.CommentAdapter
import com.teamgether.willing.LoadingDialog
import com.teamgether.willing.R
import com.teamgether.willing.databinding.ActivityOtherDetailBinding
import com.teamgether.willing.firebase.FirebaseUserService
import com.teamgether.willing.model.Comment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class OtherDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOtherDetailBinding
    private var db = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage =
        FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")
    private val storageRef: StorageReference = storage.reference

    private lateinit var adapter: CommentAdapter
    private lateinit var list: ArrayList<Comment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_other_detail)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_other_detail)
        binding.otherDetail = this

        // intent 받아오기
        val imgUrl = intent.getStringExtra("imgUrl").toString()
        val challengeId = intent.getStringExtra("challengeId").toString()
        val userName = intent.getStringExtra("userName").toString()

        binding.otherDetailUsername.text = userName

        setChallengeInfo(challengeId)
        setTimeStamp(imgUrl)
        setImg(imgUrl)

        // 뒤로가기
        binding.otherDetailBackBtn.setOnClickListener {
            finish()
        }

        // 댓글
        val inputManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        getComment(imgUrl)
        binding.otherDetailCommentList.layoutManager = LinearLayoutManager(this@OtherDetailActivity)
        binding.otherDetailSendBtn.setOnClickListener {
            saveComment(binding.otherDetailEditComment.text.toString(), imgUrl)
            binding.otherDetailEditComment.text.clear()
            inputManager.hideSoftInputFromWindow(binding.otherDetailEditComment.windowToken, 0)
        }

        // 다른 사람 프로필
        val intent = Intent(this@OtherDetailActivity, ProfileActivity::class.java)
        CoroutineScope(Dispatchers.Main).launch {
            val emailData = FirebaseUserService.getUserInfoByName(userName)
            intent.putExtra("userEmail", emailData[0]["email"].toString())
            Log.d("!!!!!!!!!!", "userEmail :: ${emailData[0]["email"].toString()}")
            binding.otherDetailUsername.setOnClickListener {
                startActivity(intent)
            }
            binding.otherDetailProfile.setOnClickListener {
                startActivity(intent)
            }
        }


    }

    // 인증사진 imageView에 넣어주기
    private fun setImg(imgUrl: String) {
        storageRef.child(imgUrl).downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(this)
                    .load(task.result)
                    .override(260, 260)
                    .centerCrop()
                    .into(binding.otherDetailImg)
            } else {
                Toast.makeText(this@OtherDetailActivity, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                Log.e("OtherDetailActivity", task.exception.toString())
            }
        }
    }

    // 해당 챌린지 정보 넣어주기
    private fun setChallengeInfo(challengeId : String) {
        db.collection("Challenge").document(challengeId).get().addOnSuccessListener { result ->
            binding.otherDetailChallenge.text = result["title"].toString()
            setProfileImg(result["uid"].toString())
        }.addOnFailureListener {
            Toast.makeText(this@OtherDetailActivity, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            Log.e("OtherDetailActivity", it.message.toString())
        }
    }

    // 해당 인증사진 타임스탬프 받아오기
    private fun setTimeStamp(imgUrl: String) {
        db.collection("Certification").whereEqualTo("imgUrl", imgUrl).get()
            .addOnSuccessListener { result ->
                val time = result.documents[0]["timestamp"].toString()
                binding.otherDetailTimesetamp.text = "${time.substring(0,4)}년 ${time.substring(4,6)}월 ${time.substring(6,8)}일 ${time.substring(8, 10)}시 ${time.substring(10)}분"
            }
            .addOnFailureListener { exception ->
                Log.e("OtherDetailActivity", "Error : $exception")
            }
    }

    // 사용자 프로필 사진 받아오기
    private fun setProfileImg(email: String) {
        var img = ""

        CoroutineScope(Dispatchers.Main).launch {
            val documents = FirebaseUserService.getUserInfoByEmail(email)
            for (document in documents) {
                img = document["profileImg"] as String

                storageRef.child(img).downloadUrl.addOnSuccessListener { task ->
                    Glide.with(applicationContext)
                        .load(task)
                        .into(binding.otherDetailProfile)
                }.addOnFailureListener {
                    Toast.makeText(this@OtherDetailActivity, "다시 시도해주세요.", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("OtherDetailActivity", it.message.toString())
                }
            }
        }

    }

    // 댓글 저장하기
    private fun saveComment(comment: String, imgUrl: String) {
        var profileImgUrl: String = ""
        var name: String = ""
        val current: Long = System.currentTimeMillis()
        val time = Date(current)
        val dateFormat = SimpleDateFormat("yy/MM/dd hh:mm")

        CoroutineScope(Dispatchers.Main).launch {
            val email = FirebaseUserService.getCurrentUser()
            val documents = FirebaseUserService.getUserInfoByName(email)
            for (document in documents) {
                name = document["name"] as String
                profileImgUrl = document["profileImg"] as String
            }

            val comments =
                Comment(imgUrl, profileImgUrl, name, comment, dateFormat.format(time), current)
            db.collection("Comment").add(comments).addOnCompleteListener {
                if (it.isSuccessful) {
                    getComment(imgUrl)
                } else {
                    Toast.makeText(this@OtherDetailActivity, "다시 시도해주세요.", Toast.LENGTH_SHORT)
                        .show()
                    Log.e("OtherDetailActivity", it.exception.toString())
                }
            }
        }
    }

    // 댓글 불러오기
    private fun getComment(imgUrl: String) {
        val dialog = LoadingDialog(this@OtherDetailActivity)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
            val documents = loadComment(imgUrl).documents
            list = arrayListOf()
            for (data in documents) {
                val comments = Comment()

                val content = data["content"] as String
                val name = data["userName"] as String
                val profileImg = data["profileImg"] as String
                val imgId = imgUrl
                val timestamp = data["timestamp"] as String

                comments.content = content
                comments.userName = name
                comments.profileImg = profileImg
                comments.imgId = imgId
                comments.timestamp = timestamp

                list.add(comments)
            }
            adapter = CommentAdapter(list)
            adapter.notifyDataSetChanged()
            binding.otherDetailCommentList.adapter = adapter

            dialog.dismiss()
        }
    }

    private suspend fun loadComment(imgUrl: String): QuerySnapshot {
        return db.collection("Comment").whereEqualTo("imgId", imgUrl).get().await()
    }

}