package com.teamgether.willing.Adapter

import android.content.Context
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
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.teamgether.willing.R
import com.teamgether.willing.model.Trial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TrialAdapter (private var list: MutableList<Trial>): RecyclerView.Adapter<TrialAdapter.ViewHolder>() {
    private val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")
    private val storageRef: StorageReference = storage.reference
    private var db = FirebaseFirestore.getInstance()

    inner class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var profileImg: ImageView = itemView!!.findViewById(R.id.item_trial_userProfile)
        var userName: TextView = itemView!!.findViewById(R.id.item_trial_userName)
        var timestamp: TextView = itemView!!.findViewById(R.id.item_trial_timestamp)
        var content: TextView = itemView!!.findViewById(R.id.item_trial_content)
        var cheeringCnt: TextView = itemView!!.findViewById(R.id.item_trial_cheeringCnt)
        var questionCnt: TextView = itemView!!.findViewById(R.id.item_trial_badCnt)
        val cheeringBtn: Button = itemView!!.findViewById(R.id.item_trial_cheeringBtn)
        val questionBtn: Button = itemView!!.findViewById(R.id.item_trial_badBtn)
        var img: ImageView = itemView!!.findViewById(R.id.item_trial_img)

        fun bind(data: Trial, context: Context) {
            storageRef.child(data.profileImg.toString()).downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .override(30,30)
                        .centerCrop()
                        .into(profileImg)
                } else {
                    Log.d("TrialAdapter !!" , task.exception.toString())
                }
            }

            storageRef.child(data.imgId.toString()).downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .override(150,150)
                        .centerCrop()
                        .into(img)
                } else {
                    Log.d("TrialAdapter !!" , task.exception.toString())
                }
            }

            userName.text= data.userName
            timestamp.text = data.timestamp
            content.text = data.content
            cheeringCnt.text = data.cheeringCnt.toString()
            questionCnt.text= data.questionCnt.toString()

            val currentUserEmail = FirebaseAuth.getInstance().currentUser.email
            var current = ""
            CoroutineScope(Dispatchers.Main).launch {
                val deferred = getCurrentUser(currentUserEmail).await().documents
                for (data in deferred) {
                    current = data["name"] as String
                }
            }

            cheeringBtn.setOnClickListener {
                cheeringCnt.text = refreshCount(data.imgId.toString(), "cheering", current, context).toString()
                notifyDataSetChanged()
            }

            questionBtn.setOnClickListener {
                questionCnt.text= refreshCount(data.imgId.toString(), "question", current, context).toString()
                notifyDataSetChanged()
            }
        }
    }

    private fun refreshCount(imgId: String, flag: String, current: String, context: Context): Int {
        var cheeringCnt = 0
        var questionCnt = 0

        plusCnt(imgId, current, context, flag)

        CoroutineScope(Dispatchers.Main).launch {
            val deferred = getArray(imgId).await().documents

            for (data in deferred) {
                val cheeringArr = data["cheering"] as ArrayList<String>
                cheeringCnt = cheeringArr.size

                val questionArr = data["question"] as ArrayList<String>
                questionCnt = questionArr.size
            }

        }
        if (flag.equals("cheering")) {
            return cheeringCnt
        } else {
            return questionCnt
        }
    }

    // 버튼
    private fun plusCnt(imgId: String, username: String, context: Context, flag: String) {
        var documentId = ""
        var array = arrayListOf<String>()

        CoroutineScope(Dispatchers.Main).launch {
            val deferred = getArray(imgId).await().documents

            for (data in deferred) {
                documentId = data.id
                array = data[flag] as ArrayList<String>
            }

            if (array.contains(username)) {
                if (flag.equals("cheering")) {
                    Toast.makeText(context, "이미 응원하기를 눌렀습니다!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "이미 의문을 남기셨습니다!", Toast.LENGTH_SHORT).show()
                }
            } else {

                array.add(username)

                db.collection("Certification").document(documentId).update(flag, array).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (flag.equals("cheering")) {
                            Toast.makeText(context, "응원을 남겼습니다!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "의문을 표했습니다!", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("TrialAdapter !!", task.exception.toString())
                    }
                }
            }

        }

    }

    private suspend fun getCurrentUser(current: String): Task<QuerySnapshot> {
        return db.collection("User").whereEqualTo("email",current).get()
    }

    private suspend fun getArray(imgId: String) : Task<QuerySnapshot> {
        return db.collection("Certification").whereEqualTo("Imgurl", imgId).get()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], holder.itemView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trial, parent, false)
        return ViewHolder(view)
    }
}