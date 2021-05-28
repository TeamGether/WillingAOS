package com.teamgether.willing.Fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.teamgether.willing.Adapter.ChallengeListAdapter
import com.teamgether.willing.LoadingDialog
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeList
import com.teamgether.willing.model.ProfileInfo
import com.teamgether.willing.view.ProfileUpdateActivity
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UserProfileFragment : Fragment() {


    private lateinit var activity: Activity
    private lateinit var list: ArrayList<ChallengeList>
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

    private lateinit var challengeListAdapter: ChallengeListAdapter
    val name = "name"
    val email = "email"
    val tobe = "tobe"
    val profileImg = "profileImg"
    private val FOLLOWER = "Follower"
    private val FOLLOWING = "Following"
    var profileImgUrl: String = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            activity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_user_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (user != null) {
            mp_follow_btn.isVisible = false
        } //팔로잉버튼 숨기기
        showLoadingDialog()
        getUserData()
        getChallenge()

        mp_profile_update_btn.setOnClickListener {
            Intent(context, ProfileUpdateActivity::class.java).apply {
                putExtra("imageUrl", profileImgUrl)
                Intent.FLAG_ACTIVITY_CLEAR_TASK
                Intent.FLAG_ACTIVITY_NO_HISTORY

            }.run {
                context?.startActivity(this)
            }
        }

        refresh_layout.setOnRefreshListener {
            getChallenge()
            challengeListAdapter.notifyDataSetChanged()
            refresh_layout.isRefreshing = false
        }
    }


    private fun getProfileImg(
        data: String,
        imageView: ImageView,
        context: UserProfileFragment
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

    private fun getUserData() {
        db.collection("User").whereEqualTo("email", user?.email).get()
            .addOnSuccessListener { result ->
                val document = result.documents[0]
                profileInfo.name = document[name] as String
                profileInfo.email = document[email] as String
                profileInfo.profileImg = document[profileImg] as String
                if (user != null) {
                    profileInfo.isMine = true
                }
                profileImgUrl = profileInfo.profileImg.toString()

                mp_email!!.text = profileInfo.email
                mp_nickName!!.text = profileInfo.name
                Log.d("profileImg", "$profileImgUrl ")

                getProfileImg(profileInfo.profileImg.toString(), mp_profile_img, this)
                Log.d("TAG", "getUserData: ${profileInfo.profileImg}")


                getFollowFollowingData(FOLLOWER, user?.email)
                getFollowFollowingData(FOLLOWING, user?.email)

            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            } //User info get from firestore db
    }

    private fun getChallenge() {
        //원래는 다른사람 피드 보면서 그 사람 페이지로 이동할 때 유저가 누구인지에 대해 값을 받아야하지만 아직 연결되지않았기 떄문에 당장은 current User로 받도록 하겠음
        val subjectField = "subject"
        val titleField = "title"
        val percentField = "percent"
        db.collection("Challenge").whereEqualTo("UID", user?.email).get()
            .addOnSuccessListener { result ->
                list = arrayListOf()
                for (document in result) {
                    val challenges = ChallengeList()
                    val documentId = document.id as String
                    val subject = document[subjectField] as String
                    val title = document[titleField] as String
                    val percent = document[percentField] as Number

                    //if문으로 background color 바꿔주기?
                    challenges.challengeId = documentId
                    challenges.subject = subject
                    challenges.title = title
                    challenges.percent = percent

                    list.add(challenges)
//                        data.percent = document[percentField] as Int
                    challengeListAdapter = ChallengeListAdapter(list, activity)
                    challengeListAdapter.notifyDataSetChanged()
                    mp_challenge_list!!.adapter = challengeListAdapter
                }
            }
    }

    private fun getFollowFollowingData(collectionName: String, email: String?) {
        db.collection(collectionName).document(email.toString()).get().addOnSuccessListener { result ->
            if (collectionName.equals(FOLLOWER)) {
                val follow = result["follower"] as ArrayList<*>
                mp_follow_user.text = follow.size.toString()

            } else {
                val following = result["following"] as ArrayList<*>
                mp_following_user.text = following.size.toString()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("TAG", "onStart: ${profileInfo.email}")
        showLoadingDialog()
        getUserData()
        Log.d("TAG", "onStart: ${profileInfo.profileImg}")

        getChallenge()
    }

    //profile 수정 시 갤러리, 촬영 등으로 이동할 수 있도록 구현 필요
    //프로필 수정 상태에 따른 변수 flag 세워서 저장 버튼 visible 관리 하기
    //콜백 처리 하든 순서를 앞으로 당기든 로딩 늦는거 해결하기
    // 로딩중 구현
    private fun showLoadingDialog() {
        val dialog = LoadingDialog(activity)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
            delay(2000)
            dialog.dismiss()
        }
    }
}