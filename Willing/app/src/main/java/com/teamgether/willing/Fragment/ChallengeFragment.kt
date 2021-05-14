package com.teamgether.willing.Fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment
import com.google.firebase.firestore.FirebaseFirestore


import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.teamgether.willing.R
import com.teamgether.willing.view.ChallengeCreateActivity
import kotlinx.android.synthetic.main.fragment_challenge.*


class ChallengeFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_challenge, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db.collection("Challenge")


//
//        rv_challenge.layoutManager = LinearLayoutManager(getActivity()?.getApplicationContext(),LinearLayoutManager.VERTICAL,false)
//        rv_challenge.setHasFixedSize(true)
//        rv_challenge.adapter = ChallengeAdapter(challengeList)
//
//
        plus_btn.setOnClickListener {
            val intent = Intent(view.context, ChallengeCreateActivity::class.java)
            Log.d(TAG, "intent tttt")
            startActivity(intent)
        }

    }

}





