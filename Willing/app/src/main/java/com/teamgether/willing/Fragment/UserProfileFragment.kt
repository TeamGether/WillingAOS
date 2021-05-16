package com.teamgether.willing.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.teamgether.willing.R
import com.teamgether.willing.model.ProfileInfo
import kotlinx.android.synthetic.main.fragment_user_profile.*

class UserProfileFragment : Fragment() {
    private var auth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage =
        FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")
    val user = auth.currentUser

    var profileInfo: ProfileInfo = ProfileInfo(
        profileImg = "",
        name = "",
        email = "",
        tobeTitleCount = mapOf("tobe" to 0),
        challengeTitlePercent = mapOf("title" to 0),
        followCount = 0,
        followerCount = 0,
        followStatus = "",
        isMine = false
    )

    val name = "name"
    val email = "email"
    val tobe = "tobe"
    val profileImg = "profileImg"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mp_profile_img.clipToOutline = true
        if(user != null){
            mp_follow_btn.isVisible = false
        }
        getUserData()
    }

    fun getUserData() {
        db.collection("User").whereEqualTo("email", user.email).get()
            .addOnSuccessListener { result ->
                val document = result.documents[0]
                Log.d("TAG", "result:${document} ")
                profileInfo.name = document[name] as String
                profileInfo.email = document[email] as String
                profileInfo.profileImg = document[profileImg] as String
                if (user != null) {
                    profileInfo.isMine = true
                }
                mp_email.text = profileInfo.email
                mp_nickName.text = profileInfo.name

                getProfleImg(profileInfo.profileImg.toString(), this)

            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

    fun getProfleImg(data: String, context: UserProfileFragment) {
        storage.reference.child(data).downloadUrl.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Glide.with(context)
                    .load(task.result)
                    .override(150, 150)
                    .centerCrop()
                    .into(mp_profile_img)
            } else {
                Log.d("error", "error:${error("")}")
            }
        }
    }

    fun followerCount(){

    }


}