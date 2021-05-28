package com.teamgether.willing.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.teamgether.willing.Adapter.FriendAdapter
import com.teamgether.willing.firebase.FirebaseFollowService
import com.teamgether.willing.firebase.FirebaseUserService
import com.teamgether.willing.model.Friends
import kotlinx.coroutines.launch

class FriendsViewModel(application: Application): AndroidViewModel(application) {
    private lateinit var list: ArrayList<Friends>
    private lateinit var adapter: FriendAdapter

    private val FOLLOWER = "Follower"
    private val FOLLOWING = "Following"

    fun loadData(recyclerView: RecyclerView) {
        viewModelScope.launch {
            val current = FirebaseUserService.getCurrentUser()

            val followers = FirebaseFollowService.getFollow("Follower", current) as ArrayList<String>
            val followings = FirebaseFollowService.getFollow("Following", current) as ArrayList<String>

            for (follower in followers) {
                for (following in followings) {
                    if (follower == following) {
                        val userInfo = FirebaseUserService.getUserInfo(follower)
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