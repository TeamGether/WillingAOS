package com.teamgether.willing.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeInfo

class ChallengeAdapter(val challengeList: ArrayList<ChallengeInfo>) : RecyclerView.Adapter<ChallengeAdapter.CustomViewHolder>()
{
    var firestore : FirebaseFirestore? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.activity_challenge_list,parent,false)
        return CustomViewHolder(view)

    }

    override fun onBindViewHolder(holder: ChallengeAdapter.CustomViewHolder, position: Int) {
        holder.title.text = challengeList.get(position).title

    }

    override fun getItemCount(): Int {
        return challengeList.size
    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.tv_title)

    }



}


