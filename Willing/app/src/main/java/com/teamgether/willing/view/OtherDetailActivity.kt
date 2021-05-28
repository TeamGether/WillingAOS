package com.teamgether.willing.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.okhttp.Dispatcher
import com.teamgether.willing.Adapter.CommentAdapter
import com.teamgether.willing.LoadingDialog
import com.teamgether.willing.R
import com.teamgether.willing.databinding.ActivityOtherDetailBinding
import com.teamgether.willing.model.Comment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.*

class OtherDetailActivity : AppCompatActivity() {
    private var challengeId: String = ""
    private var imgUrl: String = ""
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

        getData()
        setChallengeInfo()
        setTimeStamp()
        setImg()

        // 뒤로가기
        binding.otherDetailBackBtn.setOnClickListener {
            finish()
        }

        // 댓글
        val inputManager: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        getComment()
        binding.otherDetailCommentList.layoutManager = LinearLayoutManager(this@OtherDetailActivity)
        binding.otherDetailSendBtn.setOnClickListener {
            saveComment(binding.otherDetailEditComment.text.toString())
            binding.otherDetailEditComment.text.clear()
            inputManager.hideSoftInputFromWindow(binding.otherDetailEditComment.windowToken, 0)
        }

    }

    // intent 받아오기
    private fun getData() {
        if (intent.hasExtra("challengeId")) {
            challengeId = intent.getStringExtra("challengeId").toString()
            imgUrl = intent.getStringExtra("imgUrl").toString()
        } else {
            Toast.makeText(this@OtherDetailActivity, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    // 인증사진 imageView에 넣어주기
    private fun setImg() {
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
    private fun setChallengeInfo() {
        val docRef = db.collection("Challenge").document(challengeId)
        docRef.addSnapshotListener { value, error ->
            if (error != null) {
                Log.e("OtherDetailActivity", error.message.toString())
                return@addSnapshotListener
            }
            if (value != null && value.exists()) {
                binding.otherDetailChallenge.text = value["title"].toString()
                binding.otherDetailUsername.text = value["user"].toString()
                setProfileImg(value["user"].toString())

            } else {
                Toast.makeText(this@OtherDetailActivity, "다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // 해당 인증사진 타임스탬프 받아오기
    private fun setTimeStamp() {
        db.collection("Certification").whereEqualTo("Imgurl", imgUrl).get()
            .addOnSuccessListener { result ->
                val data = result.documents[0]["timestamp"].toString()
                binding.otherDetailTimesetamp.text = data
            }
            .addOnFailureListener { exception ->
                Log.e("OtherDetailActivity", "Error : $exception")
            }
    }

    // 사용자 프로필 사진 받아오기
    private fun setProfileImg(userName: String) {
        var img = ""
        db.collection("User").whereEqualTo("name", userName).get()
            .addOnSuccessListener { result ->
                img = result.documents[0]["profileImg"] as String
                storageRef.child(img).downloadUrl.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Glide.with(this)
                            .load(task.result)
                            .into(binding.otherDetailProfile)
                    } else {
                        Toast.makeText(this@OtherDetailActivity, "다시 시도해주세요.", Toast.LENGTH_SHORT)
                            .show()
                        Log.e("OtherDetailActivity", task.exception.toString())
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("OtherDetailActivity", "Error : $exception")
            }
        Log.d("OtherDetailActivity", img)

    }

    // 댓글 저장하기
    private fun saveComment(comment: String) {

        // 사용자 정보 받아오기
        val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        var profileImgUrl: String = ""
        var name: String = ""
        val current: Long = System.currentTimeMillis()
        val time = Date(current)
        val dateFormat = SimpleDateFormat("yy/MM/dd hh:mm")

        db.collection("User").whereEqualTo("email", email).get()
            .addOnSuccessListener { result ->
                name = result.documents[0]["name"] as String
                profileImgUrl = result.documents[0]["profileImg"] as String

                // db에 저장하기
                val comments =
                    Comment(imgUrl, profileImgUrl, name, comment, dateFormat.format(time), current)
                db.collection("Comment").add(comments).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        getComment()
                    } else {
                        Toast.makeText(this@OtherDetailActivity, "다시 시도해주세요.", Toast.LENGTH_SHORT)
                            .show()
                        Log.e("OtherDetailActivity", task.exception.toString())
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e("OtherDetailActivity", "Error : $exception")
            }


    }

    // 댓글 불러오기
    private fun getComment() {
        showLoadingDialog()
        db.collection("Comment").whereEqualTo("imgId", imgUrl).get()
            .addOnSuccessListener { result ->
                list = arrayListOf()
                for (data in result) {
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
//                list.sortBy { it.servertime }
//                list.asReversed()
                adapter = CommentAdapter(list)
                adapter.notifyDataSetChanged()
                binding.otherDetailCommentList.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.e("OtherDetailActivity", "Error : $exception")
            }
    }

    // 로딩중 구현
    private fun showLoadingDialog() {
        val dialog = LoadingDialog(this@OtherDetailActivity)
        CoroutineScope(Main).launch {
            dialog.show()
            delay(1500)
            dialog.dismiss()
        }
    }
}