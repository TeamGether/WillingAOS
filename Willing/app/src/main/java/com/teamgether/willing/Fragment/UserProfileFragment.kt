package com.teamgether.willing.Fragment

import ProfileChallengePagerAdapter
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.collection.LLRBNode
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
import javax.xml.parsers.ParserConfigurationException

class UserProfileFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    private lateinit var activity: Activity
    private lateinit var list: ArrayList<ChallengeList>

    private var auth = FirebaseAuth.getInstance()

    private var db = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage =
        FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")

    private val currentUser = auth.currentUser
    private var userEmail: String? = ""
    private var currentUserName: String = ""
    private var userName: String = ""
    var isMine: Boolean = false
    private var isFollow = false

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

    var profileImgUrl: String = ""

    companion object {
        fun newInstance(): UserProfileFragment = UserProfileFragment()
    }


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

        var view: View = inflater.inflate(R.layout.fragment_user_profile, container, false)
        tabLayout = view.findViewById(R.id.tab_layout)
        viewPager = view.findViewById(R.id.mp_challenge_pager)
        val adapter = ProfileChallengePagerAdapter(this)
        viewPager.adapter = adapter
        val tabName = arrayOf<String>("진헹중인 챌린지", "종료된 챌린지")

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabName[position].toString()
        }.attach()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        userEmail = this.arguments?.getString("userEmail")
        Log.d("TAG", "argument: $userEmail")

        //값을 받는 부분 여기에~~~~~~~~


        if (userEmail.equals(currentUser?.email)) {
            isMine = true
        }

        if (isMine) {
            mp_follow_btn.isVisible = false
            mp_title.setText(R.string.mp_title)

        } else {
            mp_profile_update_btn.isVisible = false
            mp_menu_btn.isVisible = false
            mp_title.setText(R.string.up_title)
        } //팔로잉버튼 숨기기
        showLoadingDialog()
        getUserData()
//        getChallenge()

        mp_profile_update_btn.setOnClickListener {
            Intent(context, ProfileUpdateActivity::class.java).apply {
                putExtra("imageUrl", profileImgUrl)
                Intent.FLAG_ACTIVITY_CLEAR_TASK
                Intent.FLAG_ACTIVITY_NO_HISTORY

            }.run {
                context?.startActivity(this)
            }
        }
        mp_follow_btn.setOnClickListener {
            if (isFollow) {
                unfollowUser()
            } else {
                followUser()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        showLoadingDialog()
        getUserData()
//        getChallenge()
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
        val nameField = "name"
        val emailField = "email"
        val tobeField = "tobe"
        val profileImg = "profileImg"


        db.collection("User").whereEqualTo("email", userEmail).get()
            .addOnSuccessListener { result ->
                val document = result.documents[0]
                profileInfo.name = document[nameField] as String
                profileInfo.email = document[emailField] as String
                profileInfo.profileImg = document[profileImg] as String

                if (currentUser != null) {
                    profileInfo.isMine = true
                }
                profileImgUrl = profileInfo.profileImg.toString()

                mp_email!!.text = profileInfo.email
                mp_nickName!!.text = profileInfo.name
                Log.d("profileImg", "$profileImgUrl ")

                getProfileImg(profileImgUrl, mp_profile_img, this)
                Log.d("TAG", "getUserData: ${profileInfo.profileImg}")
                getFollowData(userEmail.toString())
                getFollowStatus()

            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            } //User info get from firestore db
    }

/*    private fun getChallenge() {
        val subjectField = "subject"
        val titleField = "title"
        val percentField = "percent"
        db.collection("Challenge").whereEqualTo("uid", userEmail).get()
            .addOnSuccessListener { result ->
                list = arrayListOf()
                for (document in result) {
                    val challenges = ChallengeList()
                    val documentId = document.id
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
    }*/

    private fun getFollowData(email: String) {
        db.collection("Follow").document(email).get().addOnSuccessListener { result ->
            val follower = result["follower"] as ArrayList<*>?
            val following = result["following"] as ArrayList<*>?
            if (follower.isNullOrEmpty()) {
                mp_follower_user.text = "0"
            } else mp_follower_user.text = follower.size.toString()
            if (following.isNullOrEmpty()) {
                mp_following_user.text = "0"
            } else mp_following_user.text = following.size.toString()
        }

    }


    private fun showLoadingDialog() {
        val dialog = LoadingDialog(activity)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
            delay(2000)
            dialog.dismiss()
        }
    }

    //follow 상태를 가져오는 거 필요
    private fun getFollowStatus() {
        userName = profileInfo.name
        db.collection("Follow").document(currentUser?.email.toString()).get()
            .addOnSuccessListener { result ->
                val following = result["following"] as ArrayList<*>?
                isFollow = following?.contains(userName) == true
                if (isFollow) {
                    mp_follow_btn.setText(R.string.mp_did_follow)
                    mp_follow_btn.setBackgroundColor(Color.parseColor("#F0F1F2"))
                } else {
                    mp_follow_btn.setText(R.string.mp_not_follow)
                    mp_follow_btn.setBackgroundColor(Color.parseColor("#5AC5DF"))
                }
            }
        //userEmail의 name이 내 following 필드에 있는지를 검사
    }

    private fun getCurrentUserName() {
        db.collection("User").whereEqualTo("email", currentUser?.email).get()
            .addOnSuccessListener { result ->
                val document = result.documents[0]
                currentUserName = document["name"] as String
            }
    }

    private fun followUser() {
        getCurrentUserName()
        userName = profileInfo.name
        var followerList: MutableList<String> = ArrayList()
        var followingList: MutableList<String> = ArrayList()
        db.collection("Follow").document(currentUser?.email.toString()).get()
            .addOnSuccessListener { result ->
                followingList = result["following"] as ArrayList<String>
                Log.d("TAG", "followUserListBF: $followingList")
                followingList.add(userName)
                Log.d("TAG", "followUserListAF: $followingList")
                db.collection("Follow").document(currentUser?.email.toString())
                    .update("following", followingList).addOnSuccessListener {
                        getFollowData(userEmail.toString())
                        getFollowStatus()
                    }
            }

        db.collection("Follow").document(userEmail.toString()).get()
            .addOnSuccessListener { result ->
                followerList = result["follower"] as ArrayList<String>
                Log.d("TAG", "followUserBF: $followerList")
                followerList.add(currentUserName)
                Log.d("TAG", "followUserAF: $followerList")
                db.collection("Follow").document(userEmail.toString())
                    .update("follower", followerList).addOnSuccessListener {
                        getFollowData(userEmail.toString())
                        getFollowStatus()
                    }
            }

//        db.collection("Follow").document(userEmail.toString()).update("follower"))
        //그 사람의 follower에 내 이름이 추가
        //내 following에 그 사람 이름이 추가
    }

    private fun unfollowUser() {

        getCurrentUserName()
        userName = profileInfo.name
        var followerList: MutableList<String> = ArrayList()
        var followingList: MutableList<String> = ArrayList()
        db.collection("Follow").document(currentUser?.email.toString()).get()
            .addOnSuccessListener { result ->
                followingList = result["following"] as ArrayList<String>
                Log.d("TAG", "followUserListBF: $followingList")
                followingList.remove(userName)
                Log.d("TAG", "followUserListAF: $followingList")
                db.collection("Follow").document(currentUser?.email.toString())
                    .update("following", followingList).addOnSuccessListener {
                        Log.d("TAG", "followingUser: 성공성공성공")
                        getFollowData(userEmail.toString())
                        getFollowStatus()
                    }
            }

        db.collection("Follow").document(userEmail.toString()).get()
            .addOnSuccessListener { result ->
                followerList = result["follower"] as ArrayList<String>
                Log.d("TAG", "followUserBF: $followerList")
                followerList.remove(currentUserName)
                Log.d("TAG", "followUserAF: $followerList")
                db.collection("Follow").document(userEmail.toString())
                    .update("follower", followerList).addOnSuccessListener {
                        Log.d("TAG", "followerUser: 성공성공성공")
                        getFollowData(userEmail.toString())
                        getFollowStatus()
                    }
            }
        getFollowData(userEmail.toString())
        getFollowStatus()
    }
    //following -> 내가 팔로우 하는 거
    //follower -> 다른 사람이 나한테 한 거
    //follow -> following하는 행위
}//challenge 비공개인거 안불러오는 구문 추가
