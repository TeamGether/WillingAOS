package com.teamgether.willing.Adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.teamgether.willing.R
import com.teamgether.willing.model.Ranking

class RankingAdapter (private var list: MutableList<Ranking>): RecyclerView.Adapter<RankingAdapter.ViewHolder>(){
    private val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")
    private val storageRef: StorageReference = storage.reference

    inner class ViewHolder(itemView: View?): RecyclerView.ViewHolder(itemView!!) {
        var title: TextView = itemView!!.findViewById(R.id.ranking_title)

        var profileImg1: ImageView = itemView!!.findViewById<ImageView>(R.id.ranking_first_img)
        var name1: TextView = itemView!!.findViewById<TextView>(R.id.ranking_first_name)
        var profileImg2: ImageView = itemView!!.findViewById<ImageView>(R.id.ranking_second_img)
        var name2: TextView = itemView!!.findViewById<TextView>(R.id.ranking_second_name)
        var profileImg3: ImageView = itemView!!.findViewById<ImageView>(R.id.ranking_third_img)
        var name3: TextView = itemView!!.findViewById<TextView>(R.id.ranking_third_name)

        fun bind(data: Ranking, context: Context) {
            title.text = data.title

            name1.text = data.nickname_first
            name2.text = data.nickname_second
            name3.text = data.nickname_third

            storageRef.child(data.profileUrl_first.toString()).downloadUrl.addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .into(profileImg1)
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            storageRef.child(data.profileUrl_second.toString()).downloadUrl.addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .into(profileImg2)
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }

            storageRef.child(data.profileUrl_third.toString()).downloadUrl.addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .into(profileImg3)
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ranking, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RankingAdapter.ViewHolder, position: Int) {
        holder.bind(list[position], holder.profileImg1.context)
    }
}