package com.teamgether.willing.Fragment

import android.content.Intent
import android.database.DatabaseErrorHandler
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeInfo
import com.teamgether.willing.view.GroupChoiceActivity
import kotlinx.android.synthetic.main.activity_challenge_list.*
import kotlinx.android.synthetic.main.fragment_challenge.*
import kotlin.toString as toString1


class ChallengeFragment : Fragment() {

    var firestore : FirebaseFirestore? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenge, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = Firebase.firestore
        db.collection("Challenge")
        val firestore = FirebaseFirestore.getInstance()




        val tv_title = arguments?.getString("title")



        val challengeList = arrayListOf<ChallengeInfo>()
        fun notifyDataSetChanged() {

        }



        rv_challenge.layoutManager = LinearLayoutManager(getActivity()?.getApplicationContext(),LinearLayoutManager.VERTICAL,false)
        rv_challenge.setHasFixedSize(true)
        rv_challenge.adapter = ChallengeAdapter(challengeList)

        


        plus_btn.setOnClickListener {

            val intent = Intent(view.context, GroupChoiceActivity::class.java)
            startActivity(intent)
        }




    }



}





