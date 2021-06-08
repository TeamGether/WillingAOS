package com.teamgether.willing.view

import android.Manifest
import android.Manifest.permission.CAMERA
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission_group.STORAGE
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.okhttp.Challenge
import com.teamgether.willing.Fragment.UserProfileFragment
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
import java.io.File
import java.io.IOException
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

    val REQUEST_IMAGE_CAPTURE = 1
    val CAMERA_CODE = 0
    val STORAGE_CODE = 0
    lateinit var currentPhotoPath : String
    val REQUEST_TAKE_PHOTO = 22


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

        uplaod_camera_btn.setOnClickListener {
            captureCamera()
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
                        this, Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    funImageUpload(challengeId, uriPhoto)
                }
            }

        }
        if(requestCode == REQUEST_TAKE_PHOTO) {
            Log.i("REQUEST_TAKE_PHOTO", "${Activity.RESULT_OK}" + " " + "${resultCode}")
            if (resultCode == Activity.RESULT_OK) {
                try {
                    galleryAddPic()
                    Log.d("REQUEST_TAKE_PHOTO", " 저장 ")
                } catch (e: Exception) {
                    Log.e("REQUEST_TAKE_PHOTO", e.toString())
                }
            }
            else {
                Toast.makeText(this@ChallengeDetailActivity, "사진찍기를 취소하였습니다.", Toast.LENGTH_SHORT).show()
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
            val totalWeek = result["term"] as Long
            val perWeek = result["cntPerWeek"] as Long
            val bank = result["targetBank"] as String
            val account = result["targetAccount"] as String

            binding.titleTvd.text = title
            binding.moneyTvd.text = "$money"
            binding.periodTvd.text = "$totalWeek 주간 $perWeek 번씩"
            binding.accountTvd.text = "$bank $account"

            // 인증 사진 목록 불러오기
            val documents = getCertification(id).documents

            val size = documents.size
            val percent : Long  = (100 * size / (perWeek * totalWeek))

            db.collection("Challenge").document(id).update("percent", percent).addOnSuccessListener {
                Log.d("DetailActivity !!", "$percent is saved")
            }.addOnFailureListener {
                Log.e("DetailActivity !!", it.message.toString())
            }

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

    private fun getUid(cid: String) {
        val challengeInfo = ChallengeInfo()
        db.collection("Challenge").document(cid).get().addOnSuccessListener { result ->
            challengeInfo.uid = result["uid"] as String
            Log.d("TAG", "getUid: ${challengeInfo.uid}")
            userEmail = challengeInfo.uid.toString()
            Log.d("TAG", "challengeInfoUid: $userEmail ")
            if (userEmail == user?.email.toString()) {
                isMine = true
                setUI()
            }else{
                setUI()
            }
        }
    }
    private fun setUI(){
        if (isMine) {
            ch_detail_profile_cl.isVisible = false
        } else {
            ch_detail_account_cl.isVisible = false
            upload_btn.isVisible = false
            getUserData(userEmail)
        }
    }

    private fun getUserData(uid:String){
        db.collection("User").whereEqualTo("email",uid).get().addOnSuccessListener {
            result ->
            val document = result.documents[0]
            val userName = document["name"] as String?
            val profileImg = document["profileImg"] as String?

            ch_detail_name_tv.text = userName
            getProfileImg(profileImg.toString(),ch_detail_img,this)
        }
    }

    private fun getProfileImg(
        data: String,
        imageView: ImageView,
        context: ChallengeDetailActivity
    ) {
        storage.reference.child(data).downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(context)
                    .load(task.result)
                    .override(150, 150)
                    .centerCrop()
                    .into(imageView)
            } else {
                Log.e("error", "error:${error("")}")
            }
            imageView.clipToOutline = true //프로필 이미지 가장자리 클립
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
    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(READ_EXTERNAL_STORAGE, CAMERA),
            REQUEST_IMAGE_CAPTURE)
    }

    @Throws(IOException::class)
    fun createImageFile(): File? { // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_$timeStamp.jpg"
        var imageFile: File?
        val storageDir = File(Environment.getExternalStorageDirectory().toString() + "/Pictures","/willing")
        if (!storageDir.exists()) {
            Log.d("DetailActivity", storageDir.toString())
            storageDir.mkdirs()
        }
        imageFile = File(storageDir, imageFileName)
        currentPhotoPath = imageFile.absolutePath
        return imageFile
    }

    private fun captureCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File?
            try {
                photoFile = createImageFile()
            } catch (ex: IOException) {
                Log.d("DetailActivity !!", "error")
                return
            }
            if (photoFile != null) {
                val providerURI = FileProvider.getUriForFile(this,"com.teamgether.willing.view", photoFile)
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, providerURI)
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }
        }
    }

    private fun galleryAddPic() {
        Log.d("DetailActivity !!", "Call");
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        val f = File(currentPhotoPath)
        val contentUri: Uri = Uri.fromFile(f)
        mediaScanIntent.setData(contentUri)
        sendBroadcast(mediaScanIntent)
        Toast.makeText(this, "사진이 앨범에 저장되었습니다.", Toast.LENGTH_SHORT).show()
        
    }



}