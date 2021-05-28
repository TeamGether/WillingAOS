package com.teamgether.willing.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeInfo
import com.teamgether.willing.view.ChallengeDetailActivity

class ChallengeAdapter(val list: MutableList<ChallengeInfo>) : RecyclerView.Adapter<ChallengeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
        var progressBar: ProgressBar = itemView!!.findViewById(R.id.item_challengeList_progressbar)
        var title: TextView = itemView!!.findViewById(R.id.item_challengeList_title)

        fun bind(data: ChallengeInfo, context : Context) {
            title.text = data.title
            val percent = data.percent
            progressBar.incrementProgressBy(percent)

            itemView.setOnClickListener {
                Intent(context,ChallengeDetailActivity::class.java).apply {
                    putExtra("documentId", data.id)
                }.run {
                    context.startActivity(this)
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeAdapter.ViewHolder, position: Int) {
        holder.bind(list[position], holder.title.context)
    }

    override fun getItemCount(): Int {
        return list.size
    }
}