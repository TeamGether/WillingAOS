package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.teamgether.willing.MainActivity
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeInfo
import com.teamgether.willing.viewModel.ChallengeViewModel
import kotlinx.android.synthetic.main.activity_challenge_create.*
import kotlinx.android.synthetic.main.activity_challenge_create2.*
import kotlinx.android.synthetic.main.activity_challenge_create2.write_money
import java.util.*


class ChallengeCreateActivity2 : ChallengeViewModel() {

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

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                count_spinner.setSelection(0)
            }

        }
        var donate = resources.getStringArray(R.array.donationCenter_list)
        var adapter3 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, donate)
        donate_spinner.adapter = adapter3
        donate_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                donate_spinner.setSelection(0)
            }

        }

        var year = resources.getStringArray(R.array.year_list)
        var adapter4 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, year)
        year_spinner.adapter = adapter4
        year_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) = Unit

        }

        var month = resources.getStringArray(R.array.month_list)
        var adapter5 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, month)
        month_spinner.adapter = adapter5
        month_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) = Unit

        }

        var date = resources.getStringArray(R.array.date_list)
        var adapter6 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,date)
        month_spinner.adapter = adapter6
        month_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) = Unit

        }

        val money = write_money.text.toString()
        val set = write_set.text.toString()
        val detail = write_detail.text.toString()


        start_btn.setOnClickListener {

            val challengeInfo = ChallengeInfo()
            challengeInfo.title = intent.getStringExtra("title")
            Log.d("challenge2",challengeInfo.title.toString() )

            challengeInfo.reason = intent.getStringExtra("reason")
            Log.d("challenge2",challengeInfo.reason.toString() )

            challengeInfo.tobe = intent.getStringExtra("tobe")
            Log.d("challenge2",challengeInfo.tobe.toString() )


            intent.putExtra("count", count)
            intent.putExtra("doante", donate)



            Log.d("intent test - title : ", intent.getStringExtra("title").toString())

            val data = hashMapOf(
                "title" to intent.getStringExtra("title"),
                "reason" to intent.getStringExtra("reason"),
                "tobe" to intent.getStringExtra("tobe"),
                "money" to write_money.text.toString(),
                "set" to write_set.text.toString(),
                "detail" to write_detail.text.toString(),
                "period" to period_spinner.selectedItem.toString(),
                "count" to count_spinner.selectedItem.toString(),
                "doante" to donate_spinner.selectedItem.toString(),
                "year" to year_spinner.selectedItem.toString(),
                "month" to month_spinner.selectedItem.toString(),
                "date" to month_spinner.selectedItem.toString()

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


            val intent = Intent(this, ChallengeCheckActivity::class.java)
            Log.d("intent test - title : ", intent.toString())
            startActivity(intent)


        }


    }


}









