package com.teamgether.willing.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.teamgether.willing.Adapter.RankingAdapter
import com.teamgether.willing.R
import com.teamgether.willing.model.Ranking

class RankingFragment : Fragment() {

    private lateinit var adapter: RankingAdapter
    private lateinit var recyclerView: RecyclerView
    private var db = FirebaseFirestore.getInstance()
    private var storage : FirebaseStorage = FirebaseStorage.getInstance("gs://willing-88271.appspot.com")
    private val storageRef : StorageReference = storage.reference
    var list = arrayListOf<Ranking>()

    val name = "name"
    val profileImg = "profileImg"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ranking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.ranking_list)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        successKing()
        donateKing()
    }

    private fun successKing() {
        var ranking: Ranking = Ranking()

        db.collection("User").orderBy("successCount", Query.Direction.ASCENDING).limit(3)
            .get()
            .addOnSuccessListener { result ->
                val title = "이번달 성공왕"
                ranking.title = title

                val document = result.documents[0]
                val name_first = document[name] as String
                val profile_first = document[profileImg] as String
                ranking.nickname_first = name_first
                ranking.profileUrl_first = profile_first

                val document_sec = result.documents[1]
                val name_second = document_sec[name] as String
                val profile_second = document_sec[profileImg] as String
                ranking.nickname_second = name_second
                ranking.profileUrl_second = profile_second

                val document_third = result.documents[2]
                val name_third = document_third[name] as String
                val profile_third = document_third[profileImg] as String
                ranking.nickname_third = name_third
                ranking.profileUrl_third = profile_third

                list.add(ranking)
                adapter = RankingAdapter(list)
                recyclerView.adapter = adapter

                Log.d("!!!!!!!!!", ranking.toString())
            }
            .addOnFailureListener { exception ->
                Log.e("RankingFragment", "Error : $exception")
            }
    }

    private fun donateKing() {
        var ranking: Ranking = Ranking()

        db.collection("User").orderBy("successCount", Query.Direction.DESCENDING).limit(3)
            .get()
            .addOnSuccessListener { result ->
                val title = "이번달 기부왕"
                ranking.title = title

                val document = result.documents[0]
                val name_first = document[name] as String
                val profile_first = document[profileImg] as String
                ranking.nickname_first = name_first
                ranking.profileUrl_first = profile_first

                val document_sec = result.documents[1]
                val name_second = document_sec[name] as String
                val profile_second = document_sec[profileImg] as String
                ranking.nickname_second = name_second
                ranking.profileUrl_second = profile_second

                val document_third = result.documents[2]
                val name_third = document_third[name] as String
                val profile_third = document_third[profileImg] as String
                ranking.nickname_third = name_third
                ranking.profileUrl_third = profile_third

                list.add(ranking)
                adapter = RankingAdapter(list)
                recyclerView.adapter = adapter
                Log.d("!!!!!!!!!", ranking.toString())
            }
            .addOnFailureListener { exception ->
                Log.e("RankingFragment", "Error : $exception")
            }

    }



}