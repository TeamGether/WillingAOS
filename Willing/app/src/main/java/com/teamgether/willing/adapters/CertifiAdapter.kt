package com.teamgether.willing.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.teamgether.willing.R
import com.teamgether.willing.model.Certifi

class CertifiAdapter(val certifiList: ArrayList<Certifi>) : RecyclerView.Adapter<CertifiAdapter.ViewHolder>() {
    private val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")
    private val storageRef: StorageReference = storage.reference

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView = itemView.findViewById<ImageView>(R.id.item_certifi_img)
        fun bind(data: Certifi, context: Context) {
            storageRef.child(data.imgUrl).downloadUrl.addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    Glide.with(context)
                        .load(task.result)
                        .override(500,500)
                        .centerCrop()
                        .into(imageView)
                } else {
                    Toast.makeText(context, task.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CertifiAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_certifi,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CertifiAdapter.ViewHolder, position: Int) {
        holder.bind(certifiList[position], holder.imageView.context)
    }

    override fun getItemCount(): Int {
        return certifiList.size
    }
}