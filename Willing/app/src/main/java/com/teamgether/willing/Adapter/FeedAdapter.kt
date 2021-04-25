package com.teamgether.willing.Adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.teamgether.willing.R
import com.teamgether.willing.model.Feed
import com.teamgether.willing.view.OtherDetailActivity

class FeedAdapter (private var list: MutableList<Feed>): RecyclerView.Adapter<FeedAdapter.ViewHolder>(){
    private val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")
    private val storageRef: StorageReference = storage.reference

    inner class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var CertiImg: ImageView = itemView!!.findViewById<ImageView>(R.id.item_feed_img)

        fun bind(data: Feed, context: Context) {

            storageRef.child(data.pictureUrl.toString()).downloadUrl.addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .override(150, 150)
                        .centerCrop()
                        .into(CertiImg)
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            itemView.setOnClickListener{
                Intent(context, OtherDetailActivity::class.java).apply {
                    putExtra("challengeId", data.challengeId)
                    putExtra("imgUrl", data.pictureUrl)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run {
                    context.startActivity(this)
                    Log.d("Feed! :: " , data.pictureUrl + "")
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FeedAdapter.ViewHolder, position: Int) {
        holder.bind(list[position], holder.CertiImg.context)
    }


}