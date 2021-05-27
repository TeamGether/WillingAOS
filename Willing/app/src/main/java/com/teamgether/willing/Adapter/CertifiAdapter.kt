package com.teamgether.willing.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamgether.willing.R
import com.teamgether.willing.model.Certifi
import com.teamgether.willing.model.ChallengeInfo

class CertifiAdapter(val certifiList: ArrayList<Certifi>) : RecyclerView.Adapter<CertifiAdapter.ViewHolder>() {
    //private val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<TextView>(R.id.item_certifi_img)
        fun bind(data: Certifi) {
//            storage.child(data.imgUrl).downloadUrl.addOnCompleteListner{ task ->
//                if (task.isSuccessful) {
//
//                } else {
//
//                }
//            }
            //Glide.with(this).load(data.imgUrl).into(imageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertifiAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_certifi,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CertifiAdapter.ViewHolder, position: Int) {
        holder.bind(certifiList[position])
    }

    override fun getItemCount(): Int {
        return certifiList.size
    }
}