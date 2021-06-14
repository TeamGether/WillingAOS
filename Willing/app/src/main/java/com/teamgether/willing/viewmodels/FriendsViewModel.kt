package com.teamgether.willing.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.teamgether.willing.adapters.FriendAdapter
import com.teamgether.willing.firebase.FirebaseFollowService
import com.teamgether.willing.firebase.FirebaseUserService
import com.teamgether.willing.model.Friends
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class FriendsViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var list: ArrayList<Friends>
    private lateinit var adapter: FriendAdapter

    fun loadData(recyclerView: RecyclerView) {
        list = arrayListOf()
        viewModelScope.launch {
            val current = FirebaseUserService.getCurrentUser()

            val followers = FirebaseFollowService.getFollow(current, "follower")
            val followings = FirebaseFollowService.getFollow(current, "following")

            for (follower in followers) {
                for (following in followings) {
                    if (follower == following) {
                        val userInfo = FirebaseUserService.getUserInfoByName(follower)
                        setUI(userInfo)
                    }
                }
            }
            adapter = FriendAdapter(list)
            recyclerView.adapter = adapter

        }
    }

    private fun setUI(task: List<DocumentSnapshot>) {
        if (task != null) {
            for (document in task) {
                val friends = Friends()

                val profileImg = document["profileImg"] as String
                val email = document["email"] as String
                val name = document["name"] as String

                friends.profileImg = profileImg
                friends.email = email
                friends.userName = name

                list.add(friends)
            }

        }
    }

}