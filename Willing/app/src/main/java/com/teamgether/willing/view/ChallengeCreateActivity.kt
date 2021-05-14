package com.teamgether.willing.view

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.teamgether.willing.R
import com.teamgether.willing.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_challenge_create.*
import kotlinx.android.synthetic.main.activity_challenge_list.*


class ChallengeCreateActivity() : AppCompatActivity() {

    private val TAG = "Firestore"

    val items = arrayOf<String>("건강", "공부", "기타")
    val itemBank = arrayOf<String>("카카오뱅크", "농협", "신한","sc제일")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_challenge_create)


        val category = resources.getStringArray(R.array.category_list)
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, category)
        var selectCategory = ""
        category_spinner.adapter = adapter2
        category_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectCategory = items[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                category_spinner.setSelection(0)
            }
        }

        var bank = resources.getStringArray(R.array.bank_list)
        var adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bank)
        var selectBank = ""
        bank_spinner.adapter = adapter
        bank_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectBank = itemBank[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                bank_spinner.setSelection(0)
            }
        }

        next_btn.setOnClickListener {
            val title: String? = write_title.text.toString()

            var period = ""
            when (period_group.checkedRadioButtonId) {
                R.id.oneweek -> period = "총 도전 기간 ${oneweek.text}"
                R.id.twoweek -> period = "총 도전 기간 ${twoweek.text}"
                R.id.threeweek -> period = "총 도전 기간 ${threeweek.text}"
                R.id.fourweek -> period = "총 도전 기간 ${fourweek.text}"
            }



            var count = ""
            when (count_group.checkedRadioButtonId) {
                R.id.once -> count = "일주일에 도전 횟수 ${once.text}"
                R.id.threetimes -> count = "일주일에 도전 횟수${threetimes.text}"
                R.id.fivetimes -> count = "일주일에 도전 횟수 ${fivetimes.text}"
                R.id.seventimes -> count = "일주일에 도전 횟수 ${seventimes.text}"
            }


            var money= ""
            when (money_group.checkedRadioButtonId) {
                R.id.ten_thousand -> money = "도전금액 ${ten_thousand.text}"
                R.id.three_thousand -> money = "도전금액 ${three_thousand.text}"
                R.id.fifty_thousand -> money = "도전금액 ${fifty_thousand.text}"
                R.id.seventy_thousand -> money = "도전금액 ${seventy_thousand.text}"
                R.id.hundred_thousand -> money = "도전금액 ${hundred_thousand.text}"
            }


            val account: String? = write_account.text.toString()

            var expose= ""
            when (expose_group.checkedRadioButtonId) {
                R.id.open -> expose = "공개여부 ${open.text}"
                R.id.close -> expose = "공개여부 ${close.text}"

            }

            val intent = Intent(this@ChallengeCreateActivity, ChallengeCheck::class.java)
            intent.putExtra("category", selectCategory)
            Log.d("challenge1", "category :: $selectCategory")
            intent.putExtra("title", title)

            Log.d("challenge1", "title :: ${title.toString()}")

            intent.putExtra("bank", selectBank)
            Log.d("challenge1", "bank :: $selectBank")


            intent.putExtra("period", period)
            Log.d("challenge1", "period :: $period")

            intent.putExtra("count", count)
            Log.d("challenge1", "period :: $count")

            intent.putExtra("money", money)
            Log.d("challenge1", "money :: $money")

            intent.putExtra("account", account.toString())
            Log.d("challenge1", "account :: $account")

            intent.putExtra("expose", expose)
            Log.d("challenge1", "expose :: $expose")



            startActivity(intent)
        }

    }



}



