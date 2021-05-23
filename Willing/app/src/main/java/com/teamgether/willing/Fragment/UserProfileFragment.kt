package com.teamgether.willing.Fragment

import android.app.Activity
import android.content.Context
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
import com.teamgether.willing.Adapter.ChallengeListAdapter
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeList
import com.teamgether.willing.model.ProfileInfo
import kotlinx.android.synthetic.main.fragment_user_profile.*

class UserProfileFragment : Fragment() {
    private lateinit var  activity:Activity
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is Activity){
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

        mp_profile_img.clipToOutline = true //프로필 이미지 가장자리 클립

        if (user != null) {
            mp_follow_btn.isVisible = false
        } //팔로잉버튼 숨기기
        getUserData()
        getChallenge()

        refresh_layout.setOnRefreshListener {
            getChallenge()
            challengeListAdapter.notifyDataSetChanged()
            Log.d("refresh", "Refresh:")
            // 새로고침 완료시,
            // 새로고침 아이콘이 사라질 수 있게 isRefreshing = false
            refresh_layout.isRefreshing = false
        }
    }

    private fun getProfleImg(data: String, context: UserProfileFragment) {
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

    private fun getUserData() {
        db.collection("User").whereEqualTo("email", user.email).get()
            .addOnSuccessListener { result ->
                val document = result.documents[0]
                profileInfo.name = document[name] as String
                profileInfo.email = document[email] as String
                profileInfo.profileImg = document[profileImg] as String
                if (user != null) {
                    profileInfo.isMine = true
                }
                mp_email.text = profileInfo.email
                mp_nickName.text = profileInfo.name

                getProfleImg(profileInfo.profileImg.toString(), this)
                getFollowFollowingData(FOLLOWER, user.email)
                getFollowFollowingData(FOLLOWING, user.email)

            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            } //User info get from firestore db
    }

    private fun getChallenge() {
        //원래는 다른사람 피드 보면서 그 사람 페이지로 이동할 때 유저가 누구인지에 대해 값을 받아야하지만 아직 연결되지않았기 떄문에 당장은 current User로 받도록 하겠음
        val subjectField = "subject"
        val titleField = "title"
        val percentField = "percent"
        db.collection("Challenge").whereEqualTo("UID", user.email).get()
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
                    Log.d("TAG", "getChallenge: ${challenges.challengeId} ")
                    challenges.subject = subject
                    challenges.title = title
                    challenges.percent = percent

                    list.add(challenges)
//                        data.percent = document[percentField] as Int
                    challengeListAdapter = ChallengeListAdapter(list,activity)
                    challengeListAdapter.notifyDataSetChanged()
                    mp_challenge_list.adapter = challengeListAdapter
                }
            }
    }

    private fun getFollowFollowingData(collectionName: String, email: String) {
        db.collection(collectionName).document(email).get().addOnSuccessListener { result ->
            if (collectionName.equals(FOLLOWER)) {
                val follow = result["follower"] as ArrayList<String>
                mp_follow_user.text = follow.size.toString()

            } else {
                val following = result["following"] as ArrayList<String>
                mp_following_user.text = following.size.toString()
            }
        }
    }

    //profile 수정 시 갤러리, 촬영 등으로 이동할 수 있도록 구현 필요
    //프로필 수정 상태에 따른 변수 flag 세워서 저장 버튼 visible 관리 하기
    //콜백 처리 하든 순서를 앞으로 당기든 로딩 늦는거 해결하기
}