package com.teamgether.willing.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.teamgether.willing.Adapter.ChallengeListAdapter
import com.teamgether.willing.R
import com.teamgether.willing.databinding.FragmentChallengeBinding
import com.teamgether.willing.model.ChallengeInfo
import com.teamgether.willing.model.ChallengeList
import com.teamgether.willing.view.ChallengeCreateActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ChallengeFragment : Fragment() {

    private lateinit var binding : FragmentChallengeBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var list : ArrayList<ChallengeList>
    private lateinit var adapter: ChallengeListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_challenge, container, false)
        val root = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvChallenge.layoutManager = LinearLayoutManager(view.context)
        getChallengeList()

        // 챌린지 개설 버튼
        binding.plusBtn.setOnClickListener {
            startActivity(Intent(view.context, ChallengeCreateActivity::class.java))
        }

        binding.challengeSwipe.setOnRefreshListener {
            getChallengeList()
            binding.challengeSwipe.isRefreshing = false
        }

    }

    //챌린지 목록 불러오기
    private fun getChallengeList() {
        list = arrayListOf()
        CoroutineScope(Dispatchers.Main).launch {

            val current = FirebaseAuth.getInstance().currentUser?.email
            val challengeData = getData(current).await().documents

            for (data in challengeData) {
                val challenge = ChallengeList()

                val title = data["title"] as String
                val subject = data["subject"] as String
                val percent = data["percent"] as Long
                val id = data.id

                challenge.challengeId = id
                challenge.title = title
                challenge.percent = percent
                challenge.subject = subject

                list.add(challenge)
            }
            adapter = ChallengeListAdapter(list, requireContext())
            binding.rvChallenge.adapter = adapter
        }
    }

    private suspend fun getData(current : String?): Task<QuerySnapshot> {
        return db.collection("Challenge").whereEqualTo("uid", current).get()
    }

}