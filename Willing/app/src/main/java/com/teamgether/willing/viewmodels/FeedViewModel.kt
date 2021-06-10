package com.teamgether.willing.viewmodels

import android.app.Application
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.teamgether.willing.adapters.FeedAdapter
import com.teamgether.willing.firebase.FirebaseFeedService
import com.teamgether.willing.firebase.FirebaseFollowService
import com.teamgether.willing.model.Feed
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var feeds: ArrayList<Feed>
    private lateinit var feedAdapter : FeedAdapter
    var detail: MutableLiveData<List<String?>> = MutableLiveData()

    fun getData(current: String, recyclerView: RecyclerView): ArrayList<Feed> {
        feeds = arrayListOf()
        if (current.equals("recent")) {
            viewModelScope.launch {
                val deferred = FirebaseFeedService.getFeeds()
                for (data in deferred) {
                    val model = Feed(data["challengeId"].toString(), data["imgUrl"].toString(), data["timestamp"].toString())
                    feeds.add(model)
                }
                feedAdapter = FeedAdapter(feeds)
                recyclerView.adapter = feedAdapter

            }
            return feeds
        } else {
            viewModelScope.launch {
                val currentUser = FirebaseAuth.getInstance().currentUser?.email
                val followList = currentUser?.let { FirebaseFollowService.getFollow(it,"following")}
                if (followList != null) {
                    for (follow in followList) {
                        val deferred = FirebaseFeedService.getFollowFeeds(follow)
                        for (data in deferred) {
                            val model = Feed(data["challengeId"].toString(), data["imgUrl"].toString(), data["timestamp"].toString())
                            feeds.add(model)
                        }
                        feedAdapter = FeedAdapter(feeds)
                        recyclerView.adapter = feedAdapter
                    }
                }

            }
            return feeds
        }

    }
}