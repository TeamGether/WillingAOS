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

    private val db = FirebaseFirestore.getInstance()

    private val SERVER_KEY : String = "AAAATgZHayw:APA91bFBSRExqArtJs9R4EUbHGRrtvAEn6v4MbFmOly8j0Ih3CWLtpuGBpQgIn7kZ4nxG0AnsIrSfrcAZwFJgOtG9XakF5A_shWpOVqzfr7-5LjctqEwG-9eiRuaM0fY3VAMVHHnFspG"
    private val FCM_MESSAGE_URL : String = "https://fcm.googleapis.com/fcm/send"

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

    fun sendPostToFCM(userName : String?, currentUser : String?) {

        val list = getInfo(userName, currentUser)
        val fcmToken : String = list[0]
        val name : String = list[1]

        Thread() {
            kotlin.run {
                try {
                    Log.d("!!!!!", "쓰레드 실행 전!")
                    val root = JSONObject()
                    val notification = JSONObject()
                    notification.put("body", name +"님이 응원해요!")
                    notification.put("title", "Willing 윌링")
                    root.put("notification", notification)
                    root.put("to", fcmToken)

                    val url = URL(FCM_MESSAGE_URL)
                    val conn : HttpURLConnection = url.openConnection() as HttpURLConnection
                    conn.requestMethod = "POST"
                    conn.doOutput = true
                    conn.doInput = true
                    conn.addRequestProperty("Authorization", "key= $SERVER_KEY")
                    conn.setRequestProperty("Accept", "application/json")
                    conn.setRequestProperty("Content-type", "application/json")
                    val os : OutputStream = conn.outputStream
                    os.write(root.toString().toByteArray(Charset.defaultCharset()))
                    os.flush()
                    conn.responseCode
                    Log.d("!!!!!!!", "쓰레드 실행함!")
                } catch (e : Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    private fun getInfo(userName : String?, currentUser : String?): ArrayList<String> {
        var fcmToken : String = ""
        var name : String = ""
        var list : ArrayList<String> = arrayListOf()

        CoroutineScope(Dispatchers.Main).launch {
            fcmToken = getFCMToken(userName).documents[0]["fcmToken"] as String
            name = getCurrentUserData(currentUser).documents[0]["name"] as String
        }
        list.add(fcmToken)
        list.add(name)

        Log.d("!!!!!!!", "list !! : $list ")

        return list
    }


    // 현재 사용자 이름 알아오기
    private suspend fun getCurrentUserData(currentUser: String?) : QuerySnapshot {
        return db.collection("User").whereEqualTo("email", currentUser).get().await()
    }

    // 상대방 이름으로 fcm Token 알아오기
    private suspend fun getFCMToken(userName: String?): QuerySnapshot {
        return db.collection("User").whereEqualTo("name", userName).get().await()
    }

}