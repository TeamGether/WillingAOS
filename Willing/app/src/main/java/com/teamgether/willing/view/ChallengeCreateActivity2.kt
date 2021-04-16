package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import android.text.method.TextKeyListener.clear
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.teamgether.willing.MainActivity
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeInfo
import com.teamgether.willing.viewModel.ChallengeViewModel
import kotlinx.android.synthetic.main.activity_challenge_create2.*
import java.util.*


class ChallengeCreateActivity2 :  ChallengeViewModel(){

    private val TAG = "Firestore"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_create2)



        var period = resources.getStringArray(R.array.period_list)
        var adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, period)
        period_spinner.adapter = adapter2
        period_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) = Unit

        }
        var count = resources.getStringArray(R.array.count_list)
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, count)
        count_spinner.adapter = adapter
        count_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                count.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                period_spinner.setSelection(0)
            }

        }
        var donate = resources.getStringArray(R.array.donationCenter_list)
        var adapter3 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, donate)
        donate_spinner.adapter = adapter3
        donate_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                period_spinner.setSelection(0)
            }

        }

        val money = write_money.text.toString()
        val set = write_set.text.toString()
        val detail = write_detail.text.toString()

        start.setOnClickListener {

            intent.putExtra("money", money)
            intent.putExtra("set", set)
            intent.putExtra("detail", detail)
            intent.putExtra("period", period)
            intent.putExtra("count", count)
            intent.putExtra("donate", donate)


            val data = hashMapOf(
                "money" to write_money.text.toString(),
                "set" to write_set.text.toString(),
                "detail" to write_detail.text.toString(),
                "period" to period_spinner.toString(),
                "count" to count_spinner.toString(),
                "doante" to donate_spinner.toString()
            )

            val db = Firebase.firestore


            db.collection("Challenge")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }
        }



            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }




    }









