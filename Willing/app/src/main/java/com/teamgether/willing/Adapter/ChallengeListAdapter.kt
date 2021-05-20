package com.teamgether.willing.Adapter

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeList
import kotlinx.android.synthetic.main.item_challenge_mp.*

class ChallengeListAdapter(
    private var challengeList: MutableList<ChallengeList>,
    val context: Context
) :
    RecyclerView.Adapter<ChallengeListAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var subject = itemView!!.findViewById<TextView>(R.id.mp_ch_subject_tv)
        var progress = itemView!!.findViewById<ProgressBar>(R.id.mp_ch_pg_bar)
        var title = itemView!!.findViewById<TextView>(R.id.mp_challenge_title)
        val chType = itemView!!.findViewById<View>(R.id.mp_ch_subject_bg)

        fun bind(data: ChallengeList) {

            subject.text = data.subject
            title.text = data.title
            val numPercent = data.percent
            val intPercent = numPercent.toInt()
            progress.progress = intPercent

/*
            val drawable = ContextCompat.getDrawable(chType.context,R.drawable.rounded_square_type) as GradientDrawable
            when (data.subject) {
                "건강" -> drawable.setColor(ContextCompat.getColor(chType.context,R.color.opportunity))
                "공부" -> drawable.setColor(ContextCompat.getColor(chType.context,R.color.post))
                "기타" -> drawable.setColor(ContextCompat.getColor(chType.context,R.color.account))
            }
*/

            Log.d("TAG", "bind: ${subject.text}")

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