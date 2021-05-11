package com.teamgether.willing.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.tasks.Task
import com.google.android.material.behavior.SwipeDismissBehavior
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.teamgether.willing.Adapter.FeedAdapter
import com.teamgether.willing.LoadingDialog
import com.teamgether.willing.R
import com.teamgether.willing.model.Feed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FeedFragment : Fragment() {

    private var current: String = "follow"
    private lateinit var followBtn: TextView
    private lateinit var recentBtn: TextView
    private lateinit var swipeLayout: SwipeRefreshLayout
    private lateinit var feedList: RecyclerView

    private lateinit var adapter: FeedAdapter
    private var db = FirebaseFirestore.getInstance()
    private lateinit var list: ArrayList<Feed>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)

        followBtn.setOnClickListener {
            current = "follow"
            followBtn.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
            recentBtn.setTextColor(ContextCompat.getColor(context!!, R.color.black))

            refresh(view)
        }
        recentBtn.setOnClickListener {
            current = "recent"
            followBtn.setTextColor(ContextCompat.getColor(context!!, R.color.black))
            recentBtn.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))

            refresh(view)
        }

        refresh(view)

        swipeLayout.setOnRefreshListener {
            refresh(view)
            swipeLayout.isRefreshing = false
        }
    }

    private fun refresh(view: View) {
        val dialog = LoadingDialog(view.context)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
            val deferred = getDataFromFB(current).await().documents

            initData(deferred)
            dialog.dismiss()
        }
    }

    private fun init(view: View) {
        followBtn = view.findViewById(R.id.feed_followBtn)
        recentBtn = view.findViewById(R.id.feed_recentBtn)
        swipeLayout = view.findViewById(R.id.feed_swipeRefreshLayout)
        feedList = view.findViewById(R.id.feedList)

        followBtn.setTextColor(ContextCompat.getColor(context!!, R.color.colorPrimary))
        feedList.layoutManager = GridLayoutManager(view.context, 3)
    }

    suspend fun getDataFromFB(current: String): Task<QuerySnapshot> {
        if (current.equals("recent")) {
            return db.collection("Certification").orderBy("timestamp", Query.Direction.DESCENDING).get()
        } else {
            return db.collection("Certification").get()
        }
    }

    fun initData(task: MutableList<DocumentSnapshot>) {
        if (task != null) {
            list = arrayListOf()
            for (document in task) {
                val feed = Feed()

                val picture = document["Imgurl"] as String
                val id = document["challengeId"] as String
                feed.pictureUrl = picture
                feed.challengeId = id

                list.add(feed)
            }
            adapter = FeedAdapter(list)
            feedList.adapter = adapter
        }
    }
}