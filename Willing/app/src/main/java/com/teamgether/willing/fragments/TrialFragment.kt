package com.teamgether.willing.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.teamgether.willing.adapters.TrialAdapter
import com.teamgether.willing.LoadingDialog
import com.teamgether.willing.R
import com.teamgether.willing.model.Trial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TrialFragment : Fragment() {
/*
    private var db = FirebaseFirestore.getInstance()
    private lateinit var list: ArrayList<Trial>
    private lateinit var adapter: TrialAdapter
    private lateinit var trialList: RecyclerView
    private lateinit var swipeLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_trial, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        trialList = view.findViewById(R.id.trial_list)
        trialList.layoutManager = LinearLayoutManager(view.context)
        swipeLayout = view.findViewById(R.id.trial_swipeLayout)

        list = arrayListOf()
        loadData(view)

        swipeLayout.setOnRefreshListener {
            loadData(view)
            swipeLayout.isRefreshing = false
        }
    }

    private fun loadData(view: View) {
        val dialog = LoadingDialog(view.context)

        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()

            val certification = getCertiData().await().documents
            var challengeId = ""
            for (data in certification) {
                val trial = Trial()

                val imgId = data["imgUrl"] as String
                val timestamp = data["timestamp"] as Long
                val cheerCnt = data["cheering"] as ArrayList<String>
                val questionCnt = data["question"] as ArrayList<String>
                challengeId = data["challengeId"] as String

                val challengeInfo = getChallengeData(challengeId).await()
                val content = challengeInfo["title"] as String
                val name = challengeInfo["UID"]

                Log.d("!!!!!!!!!!!!!!", "${challengeInfo["title"]} !!! ${challengeInfo["UID"]}")

                val userInfo = getUserProfile(name.toString()).await().documents
                var username = ""
                var profileImg = ""
                for (data in userInfo) {
                    username = data["name"] as String
                    profileImg = data["profileImg"] as String
                }

                trial.imgId = imgId
                trial.timestamp = timestamp
                trial.cheeringCnt = cheerCnt.size.toLong()
                trial.questionCnt = questionCnt.size.toLong()
                trial.content= content
                trial.userName = username
                trial.profileImg = profileImg

                list.add(trial)
            }

            adapter = TrialAdapter(list)
            trialList.adapter = adapter

            dialog.dismiss()
        }
    }

    // 인증사진 데이터 불러오기
    private suspend fun getCertiData(): Task<QuerySnapshot> {
        return db.collection("Certification").orderBy("timestamp", Query.Direction.DESCENDING).get()
    }

    // 인증사진에 있는 챌린지 아이디 바탕으로 챌린지 정보 불러오기
    private suspend fun getChallengeData(challengeId: String): Task<DocumentSnapshot> {
        return db.collection("Challenge").document(challengeId).get()
    }

    // 챌린지 정보에 있는 사용자 이름 바탕으로 사용자 정보 불러오기
    private suspend fun getUserProfile(userName: String): Task<QuerySnapshot> {
        return db.collection("User").whereEqualTo("email", userName).get()
    }
*/
}

