package com.teamgether.willing.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeList

class ChallengeListAdapter(
    private var challengeList: MutableList<ChallengeList>
) :
    RecyclerView.Adapter<ChallengeListAdapter.ViewHolder>() {
    private var auth = FirebaseAuth.getInstance()
    private var db = FirebaseFirestore.getInstance()
    val user = auth.currentUser

    //원래는 다른사람 피드 보면서 그 사람 페이지로 이동할 때 유저가 누구인지에 대해 값을 받아야하지만 아직 연결되지않았기 떄문에 당장은 current User로 받도록 하겠음
    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var subject = itemView!!.findViewById<TextView>(R.id.mp_ch_subject_tv)
        var progress = itemView!!.findViewById<ProgressBar>(R.id.mp_ch_pg_bar)
        var title = itemView!!.findViewById<TextView>(R.id.mp_challenge_title)


        fun bind(data: ChallengeList) {
            subject.text = data.subject
            title.text = data.title
            val numPercent = data.percent
            val intPercent = numPercent.toInt()
            progress.progress = intPercent

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChallengeListAdapter.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_challenge_mp, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeListAdapter.ViewHolder, position: Int) {
        holder.bind(challengeList[position])
    }

    override fun getItemCount(): Int {
        return challengeList.size
    }

}