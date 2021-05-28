package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.teamgether.willing.R
import com.teamgether.willing.databinding.ActivityChallengeCreateBinding

class ChallengeCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengeCreateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_challenge_create)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_create)
        binding.create = this

        val category = resources.getStringArray(R.array.category_list)
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, category)
        var selectCategory = ""
        binding.categorySpinner.adapter = adapter2
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectCategory = binding.categorySpinner.getItemAtPosition(p2).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    binding.categorySpinner.setSelection(0)
                }
            }

        val bank = resources.getStringArray(R.array.bank_list)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bank)
        var selectBank = ""
        binding.bankSpinner.adapter = adapter
        binding.bankSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectBank = binding.bankSpinner.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                binding.bankSpinner.setSelection(0)
            }
        }

        binding.nextBtn.setOnClickListener {
            val title: String = binding.writeTitle.text.toString()

            // 총 도전 기간
            var total_week = 0
            when (binding.periodGroup.checkedRadioButtonId) {
                R.id.oneweek -> total_week = 1
                R.id.twoweek -> total_week = 2
                R.id.threeweek -> total_week = 3
                R.id.fourweek -> total_week = 4
            }

            // 일주일에 도전 횟수
            var per_week = 0
            when (binding.countGroup.checkedRadioButtonId) {
                R.id.once -> per_week = 1
                R.id.threetimes -> per_week = 3
                R.id.fivetimes -> per_week = 5
                R.id.seventimes -> per_week = 7
            }

            var total_count = total_week * per_week
            var count = 0
            var percent = 0

            // 금액
            var money: Int = 0
            when (binding.moneyGroup.checkedRadioButtonId) {
                R.id.ten_thousand -> money = 10000
                R.id.three_thousand -> money = 30000
                R.id.fifty_thousand -> money = 50000
                R.id.seventy_thousand -> money = 70000
                R.id.hundred_thousand -> money = 100000
            }

            // 계좌
            val account: Int = Integer.parseInt(binding.writeAccount.text.toString())

            // 공개 비공개 여부
            var show: Boolean = false
            when (binding.exposeGroup.checkedRadioButtonId) {
                R.id.open -> show = true
                R.id.close -> show = false
            }

            val intent = Intent(this@ChallengeCreateActivity, ChallengeCheckActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("per_week", per_week)
            intent.putExtra("total_count", total_count)
            intent.putExtra("show", show)
            intent.putExtra("account", account)
            intent.putExtra("count", count)
            intent.putExtra("percent", percent)
            intent.putExtra("category", selectCategory)
            intent.putExtra("bank", selectBank)
            intent.putExtra("money", money)
            startActivity(intent)
        }
    }
}