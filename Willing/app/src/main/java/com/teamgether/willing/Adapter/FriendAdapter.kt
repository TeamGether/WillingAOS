package com.teamgether.willing.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.teamgether.willing.R
import com.teamgether.willing.model.Friends

class FriendAdapter (private var list: MutableList<Friends>): RecyclerView.Adapter<FriendAdapter.ViewHolder>() {
    private val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")
    private val storageRef: StorageReference = storage.reference

    inner class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var profileImg: ImageView = itemView!!.findViewById(R.id.item_friends_profile)
        var userName: TextView = itemView!!.findViewById(R.id.item_friends_name)
        var email: TextView = itemView!!.findViewById(R.id.item_friends_email)
        var cheerUpBtn: TextView = itemView!!.findViewById(R.id.item_friends_cheerupBtn)

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
            }
            userName.text =data.userName
            email.text = data.email

            cheerUpBtn.setOnClickListener {
                // 응원하기
            }
        }
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