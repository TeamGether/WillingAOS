package com.teamgether.willing.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.teamgether.willing.Adapter.FriendAdapter
import com.teamgether.willing.LoadingDialog
import com.teamgether.willing.R
import com.teamgether.willing.model.Friends
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class FriendsFragment : Fragment() {
    private var db = FirebaseFirestore.getInstance()
    private lateinit var list: ArrayList<Friends>
    private lateinit var adapter: FriendAdapter
    private lateinit var recyclerView: RecyclerView

    private val FOLLOWER = "Follower"
    private val FOLLOWING = "Following"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_friends, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.friends_recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(view.context)


        list = arrayListOf()
        loadData(view)
    }

    private fun loadData(view : View) {
        val userEmail = FirebaseAuth.getInstance().currentUser.email
        val dialog = LoadingDialog(view.context)

        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()

            val followers = getFollow(FOLLOWER, userEmail).await()["follower"] as ArrayList<String>
            val followings = getFollow(FOLLOWING, userEmail).await()["following"] as ArrayList<String>

            for (follower in followers) {
                for (following in followings) {
                    if (follower == following) {
                        val userInfo = getUserInfo(follower).await().documents
                        setUI(userInfo)
                    }
                }
            }
            adapter = FriendAdapter(list)
            recyclerView.adapter = adapter

            dialog.dismiss()
        }
    }

    private suspend fun getFollow(path: String, userEmail : String): Task<DocumentSnapshot> {
        return db.collection(path).document(userEmail).get()
    }

    private suspend fun getUserInfo(follow: String): Task<QuerySnapshot> {
        return db.collection("User").whereEqualTo("name", follow).get()
    }

    private fun setUI(task: MutableList<DocumentSnapshot>) {
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