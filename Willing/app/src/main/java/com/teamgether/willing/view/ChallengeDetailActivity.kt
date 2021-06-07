package com.teamgether.willing.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.okhttp.Challenge
import com.teamgether.willing.LoadingDialog
import com.teamgether.willing.adapters.CertifiAdapter
import com.teamgether.willing.R
import com.teamgether.willing.databinding.ActivityChallengeDetailBinding
import com.teamgether.willing.model.Certifi
import com.teamgether.willing.model.ChallengeInfo
import kotlinx.android.synthetic.main.activity_challenge_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ChallengeDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChallengeDetailBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var list: ArrayList<Certifi>
    private lateinit var adapter: CertifiAdapter
    private var challengeId: String = ""
    private var userEmail: String = ""
    private val storage: FirebaseStorage =
        FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")


    private var uid: String? = null
    private var auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    var storageRef: StorageReference? = null

    var pickImageFromAlbum = 0
    var fbStorage: FirebaseStorage? = null
    var uriPhoto: Uri? = null

    private var isMine: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_challenge_detail)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_detail)
        binding.challengeDetail = this
        binding.detailList.layoutManager = GridLayoutManager(this, 3)

        uid = FirebaseAuth.getInstance().currentUser?.uid
        fbStorage = FirebaseStorage.getInstance()
        storageRef = fbStorage!!.getReference()

        challengeId = intent.getStringExtra("challengeId").toString()
//        Log.d("!!!!!!!Detail!!!!", "id :: $challengeId")
        if (challengeId != null) {
            upload(challengeId!!)
        }

        getUid(challengeId)

        //다른 사람 프로필로 이동 구현하기
        ch_detail_img.setOnClickListener {
            val intent = Intent(this,ProfileActivity::class.java)
            intent.putExtra("userEmail",userEmail)
            startActivity(intent)
        }

        ch_detail_fork_btn.setOnClickListener {
            moveActivity()
        }

        upload_btn.setOnClickListener {

            val photoPickerIntent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type = "image/*"
            startActivityForResult(photoPickerIntent, pickImageFromAlbum)

        }
    }

    private fun getUid(cid: String) {
        var challengeInfo = ChallengeInfo()
        db.collection("Challenge").document(cid).get().addOnSuccessListener { result ->
            challengeInfo.uid = result["uid"] as String
            Log.d("TAG", "getUid: ${challengeInfo.uid}")
            userEmail = challengeInfo.uid.toString()
            Log.d("TAG", "challengeInfoUid: $userEmail ")
            if (userEmail == user?.email.toString()) {
                isMine = true
                setUI()
            }
        }
    }
    private fun setUI(){
        if (isMine) {
            ch_detail_profile_cl.isVisible = false
//            Log.d("TAG", "onCreate: $isMine")

        } else {
            ch_detail_account_cl.isVisible = false
            upload_btn.isVisible = false
        }
    }

    private fun moveActivity(){
        val intent = Intent(this, ChallengeCreateActivity::class.java)
        intent.putExtra("challengeId",challengeId)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickImageFromAlbum) {
            if (resultCode == Activity.RESULT_OK) {
                uriPhoto = data?.data
//                Log.d("!!!!!!!!!", "$uriPhoto !!")

                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    funImageUpload(challengeId, uriPhoto)
                }
            }

        }
    }


    private fun funImageUpload(challengeId: String?, uri: Uri?) {
        var timeStamp = SimpleDateFormat("yyyyMMddHHmm").format(Date())
        var imgFileName = "IMAGE" + timeStamp + "_.png"
        var storageRef = fbStorage?.reference?.child("certification")?.child(imgFileName)

        storageRef?.putFile(uri!!)?.addOnSuccessListener {
            Toast.makeText(this, "upload", Toast.LENGTH_SHORT).show()

            val certifi = Certifi()
            certifi.imgUrl = "certification/$imgFileName"
            certifi.timestamp = timeStamp
            certifi.challengeId = challengeId
            certifi.cheering = arrayListOf()
            certifi.question = arrayListOf()

            val userEmail = FirebaseAuth.getInstance().currentUser?.email
            CoroutineScope(Dispatchers.Main).launch {
                val deferred = getUserName(userEmail).documents[0]
                certifi.userName = deferred["name"] as String

                db.collection("Certification").add(certifi).addOnSuccessListener {
                    upload(challengeId!!)
                }.addOnFailureListener {
                    Log.e("DetailACtvity!!", it.message.toString())
                }
            }
        }?.addOnFailureListener {
            Log.e("!!!!!!!", it.message.toString())
        }


    }

    private fun upload(id: String) {
        val dialog = LoadingDialog(this)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()

            //챌린지 정보 불러오기
            val result = getChallenge(id)

            val title = result["title"] as String
            val money = result["price"] as Long
            val totalWeek = result["term"]
            val perWeek = result["cntPerWeek"]
            val bank = result["targetBank"]
            val account = result["targetAccount"]

            binding.titleTvd.text = title
            binding.moneyTvd.text = "$money"
            binding.periodTvd.text = "$totalWeek 주간 $perWeek 번씩"
            binding.accountTvd.text = "$bank $account"

            // 인증 사진 목록 불러오기
            val documents = getCertification(id).documents
            list = arrayListOf()
            for (document in documents) {
                val model =
                    Certifi(document["imgUrl"].toString(), document["timestamp"].toString())
                list.add(model)
            }
            adapter = CertifiAdapter(list)
            binding.detailList.adapter = adapter

            dialog.dismiss()
        }

    }

    private suspend fun getUserName(email: String?): QuerySnapshot {
        return db.collection("User").whereEqualTo("email", email).get().await()
    }

    private suspend fun getChallenge(id: String): DocumentSnapshot {
        return db.collection("Challenge").document(id).get().await()
    }

    private suspend fun getCertification(id: String): QuerySnapshot {
        return db.collection("Certification").whereEqualTo("challengeId", id).get().await()
    }
}