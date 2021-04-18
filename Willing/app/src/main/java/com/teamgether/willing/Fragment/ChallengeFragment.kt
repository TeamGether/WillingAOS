package com.teamgether.willing.Fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import androidx.fragment.app.Fragment


import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.squareup.okhttp.Challenge
import com.teamgether.willing.R
import com.teamgether.willing.view.GroupChoiceActivity
import kotlinx.android.synthetic.main.activity_challenge_create.*
import kotlinx.android.synthetic.main.fragment_challenge.*
import kotlinx.android.synthetic.main.fragment_challenge.view.*


class ChallengeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenge, container, false)

    }

    val list: MutableList<Challenge> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        db.collection("Challenge")

        refresh_btn.setOnClickListener {



        }

        plus_btn.setOnClickListener {
            var intent = Intent(view.context, GroupChoiceActivity::class.java)
            startActivity(intent)
        }
    }

    private fun listRefresh() {
        list.removeAll { true }

        abstract class lstAdp : BaseAdapter() {
            override fun getCount(): Int {
                return list.size
            }


            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

                var view: View? = convertView
                if (convertView == null) {
                    view = layoutInflater.inflate(R.layout.fragment_challenge, null)
                }

                var txt1 = view?.findViewById<TextView>(R.id.working_list)

                txt1?.text = "${list[position].toString()} "


                return view
            }


        }
    }
}





