package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.database.*
import com.google.firestore.v1.DocumentTransform
import com.teamgether.willing.MainActivity
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeInfo
import com.teamgether.willing.viewModel.ChallengeViewModel
import kotlinx.android.synthetic.main.activity_challenge_create2.*
import java.sql.Types.TIMESTAMP
import java.util.*

class ChallengeCreateActivity2 :  ChallengeViewModel(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_create2)

        val challengeInfo = ChallengeInfo()
//        challengeInfo.writeTime = DocumentTransform.FieldTransform.ServerValue.TIMESTAMP


        var period_data = resources.getStringArray(R.array.period_list)
        var adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,period_data)
        period_sinner.adapter = adapter2
        period_sinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        var donationCenter_data = resources.getStringArray(R.array.donationCenter_list)
        var adapter3 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,donationCenter_data)
        donate_spinner.adapter = adapter3
        donate_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
               override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }

        var yearList = (2020..2900).toList()
        var monthList = (1..12).toList()
        var dateList = (1..31).toList()
        var yearStrConvertList = yearList.map { it.toString() }
        var monthStrConvertList = monthList.map { it.toString() }
        var dateStrConvertList = dateList.map { it.toString() }

        picker_year.run {
        minValue = 0
        maxValue = yearStrConvertList.size - 1
        wrapSelectorWheel = false
        displayedValues = yearStrConvertList.toTypedArray() }
        picker_month.run {
            minValue = 0
            maxValue = monthStrConvertList.size - 1
            wrapSelectorWheel = false
        displayedValues = monthStrConvertList.toTypedArray() }
        picker_date.run {
            minValue = 0
            maxValue = dateStrConvertList.size - 1
            wrapSelectorWheel = false
        displayedValues = dateStrConvertList.toTypedArray() }





        start.setOnClickListener {

            val money = write_money.text.toString()
            val set = write_set.text.toString()
            val goal_number =number_sinner.toString()
            val period =period_sinner.toString()
            val donation =donate_spinner.toString()

            setData2(money,set,goal_number,period,donation)


            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }




    }

    private fun setData2(
        money: String,
        goalNumber: String,
        period: String,
        donation: String,
        donation1: String
    ) {


    }
}




