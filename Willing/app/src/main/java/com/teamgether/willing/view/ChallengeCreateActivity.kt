package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.teamgether.willing.R
import com.teamgether.willing.databinding.ActivityChallengeCreateBinding

class ChallengeCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengeCreateBinding
    private var auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    private var uid : String? = null
    private val storage: FirebaseStorage =
        FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")
    private val storageRef: StorageReference = storage.reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_challenge_create)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_create)
        binding.create = this

        uid = FirebaseAuth.getInstance().currentUser?.uid

        val email = user?.email

        val category = resources.getStringArray(R.array.category_list)
        val adapter2 = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, category)
        var subject = ""
        binding.categorySpinner.adapter = adapter2
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    subject = binding.categorySpinner.getItemAtPosition(p2).toString()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    binding.categorySpinner.setSelection(0)
                }
            }

        val bank = resources.getStringArray(R.array.bank_list)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bank)
        var targetBank = ""
        binding.bankSpinner.adapter = adapter
        binding.bankSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                targetBank = binding.bankSpinner.getItemAtPosition(p2).toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                binding.bankSpinner.setSelection(0)
            }
        }

        binding.nextBtn.setOnClickListener {
            val title: String = binding.writeTitle.text.toString()

            // 총 도전 기간
            var term = 0
            when (binding.periodGroup.checkedRadioButtonId) {
                R.id.oneweek -> term = 1
                R.id.twoweek -> term = 2
                R.id.threeweek -> term = 3
                R.id.fourweek -> term = 4
            }

            // 일주일에 도전 횟수
            var cntPerWeek : Int = 0
            when (binding.countGroup.checkedRadioButtonId) {
                R.id.once -> cntPerWeek = 1
                R.id.threetimes -> cntPerWeek = 3
                R.id.fivetimes -> cntPerWeek = 5
                R.id.seventimes -> cntPerWeek = 7
            }

            var percent = 0

            // 금액
            var price : Int = 0
            when (binding.moneyGroup.checkedRadioButtonId) {
                R.id.ten_thousand -> price = 10000
                R.id.three_thousand -> price= 30000
                R.id.fifty_thousand -> price= 50000
                R.id.seventy_thousand -> price = 70000
                R.id.hundred_thousand -> price = 100000
            }

            // 계좌
            val targetAccount = binding.writeAccount.text.toString()

            // 공개 비공개 여부
            var show: Boolean = false
            when (binding.exposeGroup.checkedRadioButtonId) {
                R.id.open -> show = true
                R.id.close -> show = false
            }

            val intent = Intent(this@ChallengeCreateActivity, ChallengeCheckActivity::class.java)
            intent.putExtra("title", title)
            intent.putExtra("cntPerWeek", cntPerWeek)
            intent.putExtra("show", show)
            intent.putExtra("targetAccount", targetAccount)
            intent.putExtra("percent", percent)
            intent.putExtra("subject", subject)
            intent.putExtra("targetBank", targetBank)
            intent.putExtra("price", price)
            intent.putExtra("term", term)
            intent.putExtra("uid", email)
            startActivity(intent)
        }
    }
}