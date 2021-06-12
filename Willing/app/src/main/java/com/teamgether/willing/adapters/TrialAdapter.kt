package com.teamgether.willing.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.teamgether.willing.R
import com.teamgether.willing.firebase.FirebaseUserService
import com.teamgether.willing.fragments.TrialFragment
import com.teamgether.willing.model.Trial
import com.teamgether.willing.view.ProfileActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class TrialAdapter (private var list: MutableList<Trial>, var link : TrialFragment.SetListAdapter): RecyclerView.Adapter<TrialAdapter.ViewHolder>() {
    private val storage: FirebaseStorage =
        FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")
    private val storageRef: StorageReference = storage.reference
    private var db = FirebaseFirestore.getInstance()

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        val profileImg: ImageView = itemView!!.findViewById(R.id.item_trial_userProfile)
        val userName: TextView = itemView!!.findViewById(R.id.item_trial_userName)
        val timestamp: TextView = itemView!!.findViewById(R.id.item_trial_timestamp)
        val content: TextView = itemView!!.findViewById(R.id.item_trial_content)
        val cheeringCnt: TextView = itemView!!.findViewById(R.id.item_trial_cheeringCnt)
        val questionCnt: TextView = itemView!!.findViewById(R.id.item_trial_badCnt)
        val cheeringBtn: Button = itemView!!.findViewById(R.id.item_trial_cheeringBtn)
        val questionBtn: Button = itemView!!.findViewById(R.id.item_trial_badBtn)
        val img: ImageView = itemView!!.findViewById(R.id.item_trial_img)
        val layout : LinearLayout = itemView!!.findViewById(R.id.item_trial_profileLayout)

        fun bind(data: Trial, context: Context) {
            storageRef.child(data.profileImg.toString()).downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .format(DecodeFormat.PREFER_ARGB_8888)
                        .override(30, 30)
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(profileImg)
                } else {
                    Log.d("TrialAdapter !!", task.exception.toString())
                }
            }

            storageRef.child(data.imgId.toString()).downloadUrl.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .into(img)
                } else {
                    Log.d("TrialAdapter !!", task.exception.toString())
                }
            }

            userName.text = data.userName
            val time = data.timestamp.toString()
            timestamp.text = "${time.substring(0,4)}년 ${time.substring(4,6)}월 ${time.substring(6,8)}일 ${time.substring(8, 10)}시 ${time.substring(10)}분"
            content.text = data.content
            cheeringCnt.text = data.cheeringCnt.toString()
            questionCnt.text = data.questionCnt.toString()

            CoroutineScope(Dispatchers.Main).launch {

                val emailData = FirebaseUserService.getUserInfoByName(data.userName!!)
                val intent = Intent(context, ProfileActivity::class.java)
                for (email in emailData) {
                    intent.putExtra("userEmail", email["email"].toString())
                }
                layout.setOnClickListener {
                    context.startActivity(intent)
                }
            }


        }
    }

    // 버튼
    private fun plusCnt(imgId: String, username: String, context: Context, flag: String) {
        var documentId = ""
        var array = arrayListOf<String>()

        CoroutineScope(Dispatchers.Main).launch {
            val deferred = getArray(imgId).documents

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

                db.collection("Certification").document(documentId).update(flag, array)
                    .addOnCompleteListener { task ->
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

    private suspend fun getArray(imgId: String): QuerySnapshot {
        return db.collection("Certification").whereEqualTo("imgUrl", imgId).get().await()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position], holder.itemView.context)
        holder.cheeringBtn.setOnClickListener {
            list[position].userName?.let { it1 ->
                plusCnt(list[position].imgId.toString(),
                    it1, holder.itemView.context, "cheering")
            }
            link.getData(holder.itemView.context)
        }

        holder.questionBtn.setOnClickListener {
            list[position].userName?.let { it1 ->
                plusCnt(list[position].imgId.toString(),
                    it1, holder.itemView.context, "question")
            }

            link.getData(holder.itemView.context)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trial, parent, false)
        return ViewHolder(view)
    }

}