 package com.teamgether.willing.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.teamgether.willing.R
import com.teamgether.willing.firebase.FirebaseUserService
import com.teamgether.willing.model.Friends
import com.teamgether.willing.view.OtherDetailActivity
import com.teamgether.willing.view.ProfileActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

class FriendAdapter (private var list: MutableList<Friends>): RecyclerView.Adapter<FriendAdapter.ViewHolder>() {
    private val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")
    private val storageRef: StorageReference = storage.reference

    private val db = FirebaseFirestore.getInstance()

    inner class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var profileImg: ImageView = itemView!!.findViewById(R.id.item_friends_profile)
        var userName: TextView = itemView!!.findViewById(R.id.item_friends_name)
        var email: TextView = itemView!!.findViewById(R.id.item_friends_email)
        var cheering : Button = itemView!!.findViewById(R.id.item_friends_cheerupBtn)

        fun bind(data: Friends, context: Context) {
            storageRef.child(data.profileImg.toString()).downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .override(50,50)
                        .centerCrop()
                        .into(profileImg)
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
                profileImg.clipToOutline = true
            }
            userName.text =data.userName
            email.text = data.email

            itemView.setOnClickListener {
                val intent = Intent(context, ProfileActivity::class.java)
                intent.putExtra("userEmail", data.email)
                context.startActivity(intent)
            }

            // 응원하기 이벤트
            cheering.setOnClickListener {
                val current = FirebaseUserService.getCurrentUser()
                db.collection("User").whereEqualTo("email", current).get().addOnSuccessListener { result ->
                    val documents = result.documents
                    var userName : String = ""
                    var fcmToken : String = ""
                    for (document in documents) {
                        userName = document["name"] as String
                        fcmToken = document["fcmToken"] as String
                    }
                    Thread() {
                        kotlin.run {
                            try {
                                sendNotification(fcmToken, userName)
                            } catch (e : Exception) {
                                e.printStackTrace()
                            }
                        }
                    }.start()

                }

                Toast.makeText(itemView.context, "응원하였습니다!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun sendNotification(friend : String, userName : String) {
        val root = JSONObject()
        val notification = JSONObject()
        notification.put("body", "$userName 님이 응원해요!")
        notification.put("title", "Willing 윌링")
        root.put("notification", notification)
        root.put("to", friend)

        val url = URL("https://fcm.googleapis.com/fcm/send")
        val conn : HttpURLConnection = url.openConnection() as HttpURLConnection
        conn.requestMethod = "POST"
        conn.doOutput = true
        conn.doInput = true
        conn.addRequestProperty("Authorization", "key= AAAATgZHayw:APA91bFBSRExqArtJs9R4EUbHGRrtvAEn6v4MbFmOly8j0Ih3CWLtpuGBpQgIn7kZ4nxG0AnsIrSfrcAZwFJgOtG9XakF5A_shWpOVqzfr7-5LjctqEwG-9eiRuaM0fY3VAMVHHnFspG")
        conn.setRequestProperty("Accept", "application/json")
        conn.setRequestProperty("Content-type", "application/json")
        val os : OutputStream = conn.outputStream
        os.write(root.toString().toByteArray(Charset.defaultCharset()))
        os.flush()
        conn.responseCode
    }



    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], holder.itemView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_friends, parent, false)
        return ViewHolder(view)
    }

}