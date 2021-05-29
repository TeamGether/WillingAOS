package com.teamgether.willing.view

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.teamgether.willing.R
import kotlinx.android.synthetic.main.activity_profile_update.*
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import kotlinx.coroutines.*

class ProfileUpdateActivity : AppCompatActivity() {
    private val storage: FirebaseStorage =
        FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")

    private var auth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()
    private val user = auth.currentUser!!

    var fileName: String = ""
    var profileUpdateUrl: String = ""
    var uri: Uri? = null
    private val REQUEST_GET_IMAGE = 105
    private val CAMERA_CODE = 98
    private val STORAGE_CODE = 99
    private val CAMERA = arrayOf(Manifest.permission.CAMERA)
    private val STORAGE = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_update)


        profileUpdateUrl = intent.getStringExtra("imageUrl").toString()
        getProfileImg(profileUpdateUrl, profile_update_img, this)


        profile_update_cancel_btn.setOnClickListener {
        }

        profile_update_save_btn.setOnClickListener {
            uploadImage()
        }

        camera_btn.setOnClickListener {
            callCamera()
        }
        gallery_btn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_GET_IMAGE)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) Toast.makeText(
                        this,
                        "camera permission ??",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            STORAGE_CODE -> {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) Toast.makeText(
                        this,
                        "storage permission ??",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_GET_IMAGE -> {
                    try {
                        uri = data?.data
                        profile_update_img.setImageURI(uri)
                    } catch (e: Exception) {
                    }
                }
                CAMERA_CODE -> {
                    if (data?.extras?.get("data") != null) {
                        val img = data.extras?.get("data") as Bitmap
                        uri = saveFile(randomFileName(), "image/jpeg", img)
                        profile_update_img.setImageURI(uri)
                    }
                }
            }
        }
    }

    private fun checkPermission(permissions: Array<out String>, type: Int): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in permissions) {
                if (ContextCompat.checkSelfPermission(
                        this,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    ActivityCompat.requestPermissions(this, permissions, type)
                    return false;
                }
            }
        }
        return true;
    }

    private fun callCamera() {
        if (checkPermission(CAMERA, CAMERA_CODE) && checkPermission(STORAGE, STORAGE_CODE)) {
            val itt = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(itt, CAMERA_CODE)
        }
    }

    private fun getProfileImg(
        data: String,
        imageView: ImageView,
        context: Context
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

    private fun randomFileName(): String {
        fileName = SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())
        return fileName
    }

    private fun saveFile(fileName: String, mimeType: String, bitmap: Bitmap): Uri? {
        var CV = ContentValues()
        CV.put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
        CV.put(MediaStore.Images.Media.MIME_TYPE, mimeType)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            CV.put(MediaStore.Images.Media.IS_PENDING, 1)
        }
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CV)
        if (uri != null) {
            var scriptor = contentResolver.openFileDescriptor(uri, "w")
            if (scriptor != null) {
                val fos = FileOutputStream(scriptor.fileDescriptor)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    CV.clear()
                    CV.put(MediaStore.Images.Media.IS_PENDING, 0)
                    contentResolver.update(uri, CV, null, null)
                }
            }
        }
        profileUpdateUrl = uri.toString()
        return uri;
    }

    private fun uploadImage() {
        randomFileName()
        val storageRef = storage.reference.child("profile/${fileName}" + ".jpeg")
        storageRef.putFile(uri!!).addOnSuccessListener {
            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
            findDocument()
            Log.d("TAG", "uploadImage: upload")
        }
    }

    private fun findDocument() {
        db.collection("User").whereEqualTo("email", user.email).get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val documentId = document.id as String
                    updateProfileImgField(documentId)

                }
            }

    }

    private fun updateProfileImgField(documentId: String) {
        db.collection("User").document(documentId).update(
            "profileImg",
            "profile/${fileName}" + ".jpeg"
        ).addOnSuccessListener {
            moveActivity()
            Toast.makeText(this, "$uri", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Log.e("TAG", "updateUserProfileImg: ${error("")}")
        }
    }


    private fun moveActivity() {
        Log.d("TAG", "moveActivity: finish")
        Intent.FLAG_ACTIVITY_CLEAR_TASK
        finish()
    }

}
//fucking
// corutine
