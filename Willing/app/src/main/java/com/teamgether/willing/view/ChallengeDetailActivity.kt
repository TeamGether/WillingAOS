package com.teamgether.willing.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.teamgether.willing.adapters.CertifiAdapter
import com.teamgether.willing.R
import com.teamgether.willing.databinding.ActivityChallengeDetailBinding
import com.teamgether.willing.model.Certifi

class ChallengeDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityChallengeDetailBinding
    private val db = FirebaseFirestore.getInstance()
    private val list : ArrayList<Certifi> = arrayListOf()
    private lateinit var adapter : CertifiAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_challenge_detail)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_detail)
        binding.challengeDetail = this
        binding.detailList.layoutManager = GridLayoutManager(this, 3)

        val id = intent.getStringExtra("id")

        if (id != null) {
            //챌린지 정보 불러오기
            db.collection("Challenge").document(id).get()
                .addOnSuccessListener { result ->
                    val title = result["title"] as String
                    val money = result["money"] as String
                    val totalWeek = result["total_week"]
                    val perWeek = result["per_week"]

                    val bank = result["bank"]
                    val account = result["account"]

                    binding.titleTvd.text = title
                    binding.moneyTvd.text = money
                    binding.periodTvd.text = "$totalWeek 주간 $perWeek 번씩"
                    binding.accountTvd.text = "$bank $account"
                }


            // 인증 사진 목록 불러오기
            db.collection("Certification").whereEqualTo("challengeId", id).get()
                .addOnSuccessListener { result ->
                    val documents = result.documents

                    for (document in documents) {
                        val model = Certifi(document["Imgurl"].toString(), document["timestamp"].toString())
                        list.add(model)
                    }
                    adapter = CertifiAdapter(list)
                    binding.detailList.adapter = adapter
                }
        }


    }
}