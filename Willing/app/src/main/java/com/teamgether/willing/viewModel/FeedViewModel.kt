package com.teamgether.willing.viewModel

import android.app.Application
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.teamgether.willing.Adapter.FeedAdapter
import com.teamgether.willing.firebase.FirebaseFeedService
import com.teamgether.willing.model.Feed
import kotlinx.coroutines.launch

class FeedViewModel(application: Application) : AndroidViewModel(application) {

    private val feeds: ArrayList<Feed> = arrayListOf()
    private lateinit var feedAdapter : FeedAdapter
    var detail: MutableLiveData<List<String?>> = MutableLiveData()

    fun getData(current: String, recyclerView: RecyclerView): ArrayList<Feed> {
        viewModelScope.launch {
            val deferred = FirebaseFeedService.getFeeds()
            for (data in deferred) {
                val model = Feed(data["challengeId"].toString(), data["Imgurl"].toString(), data["timestamp"].toString())
                feeds.add(model)
            }
            feedAdapter = FeedAdapter(feeds)
            recyclerView.adapter = feedAdapter

        }
        return feeds
    }
}