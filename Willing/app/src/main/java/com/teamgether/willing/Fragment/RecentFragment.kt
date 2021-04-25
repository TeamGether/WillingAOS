package com.teamgether.willing.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.teamgether.willing.Adapter.FeedAdapter
import com.teamgether.willing.R
import com.teamgether.willing.model.Feed

class RecentFragment : Fragment() {

    private lateinit var adapter: FeedAdapter
    private lateinit var recyclerView: RecyclerView
    private var db = FirebaseFirestore.getInstance()
    private lateinit var list: ArrayList<Feed>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recent, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recentList)
        recyclerView.layoutManager = GridLayoutManager(view.context, 3)

        getFeed()

    }

    private fun getFeed() {

        db.collection("Certification").orderBy("timestamp", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { result ->
                list = arrayListOf()
                for (document in result) {
                    val feed = Feed()

                    val picture = document["Imgurl"] as String
                    val id = document["challengeId"] as String
                    feed.pictureUrl = picture
                    feed.challengeId = id

                    list.add(feed)
                }
                adapter = FeedAdapter(list)
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.e("RankingFragment", "Error : $exception")
            }
    }

    fun newInstance(): RecentFragment
    {
        val args = Bundle()
        val frag = RecentFragment()
        frag.arguments = args
        return frag
    }
}