package com.teamgether.willing.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckedTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isEmpty
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.teamgether.willing.LoadingDialog
import com.teamgether.willing.R
import com.teamgether.willing.databinding.ActivityChallengeCreateBinding
import com.teamgether.willing.model.ChallengeInfo
import kotlinx.android.synthetic.main.activity_challenge_create.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChallengeCreateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChallengeCreateBinding
    private val db = FirebaseFirestore.getInstance()
    private var auth = FirebaseAuth.getInstance()
    val user = auth.currentUser
    private var uid: String? = null
    private val storage: FirebaseStorage =
        FirebaseStorage.getInstance("gs://willing-88271.appspot.com/")
    private val storageRef: StorageReference = storage.reference
    private var challengId: String = ""
    private var isFork: Boolean = false
    var subject = ""
    var targetBank = ""

    var challengeInfo = ChallengeInfo()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_challenge_create)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_challenge_create)
        binding.create = this
        getForkStatus()
        //spinner Adapter 뜯어보기
        //초기값 세팅하기
        Log.d("TAG", "onCreate:$isFork ") //true 잘 나옴
        uid = FirebaseAuth.getInstance().currentUser?.uid
        val email = user?.email

        binding.createBackBtn.setOnClickListener {
            goBackActivity()
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
            var cntPerWeek: Int = 0
            when (binding.countGroup.checkedRadioButtonId) {
                R.id.once -> cntPerWeek = 1
                R.id.threetimes -> cntPerWeek = 3
                R.id.fivetimes -> cntPerWeek = 5
                R.id.seventimes -> cntPerWeek = 7
            }
            var percent = 0
            // 금액
            var price: Int = 0
            when (binding.moneyGroup.checkedRadioButtonId) {
                R.id.ten_thousand -> price = 10000
                R.id.three_thousand -> price = 30000
                R.id.fifty_thousand -> price = 50000
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

//            moveActivity(
//                title,
//                cntPerWeek,
//                show,
//                targetAccount,
//                percent,
//                subject,
//                targetBank,
//                price,
//                term,
//                email
//            )

            if(title.isNotEmpty()&& targetAccount.isNotEmpty()){
                moveActivity(title, cntPerWeek, show,
                    targetAccount,
                    percent,
                    subject,
                    targetBank,
                    price,
                    term,
                    email)
            }else {
                Toast.makeText(this,"비어있는 값을 채우세요", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "challenge: 널체크!!! ")

            }



        }



    }

    private fun setUpCategorySpinner() {
        val subjects = resources.getStringArray(R.array.category_list)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, subjects)
        binding.categorySpinner.adapter = adapter
        if (isFork) {
            val forkPos: Int = adapter.getPosition(challengeInfo.subject)
            setForkedValue(forkPos)
        }


    }


    private fun setUpBankSpinner() {
        val banks = resources.getStringArray(R.array.bank_list)
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, banks)
        binding.bankSpinner.adapter = adapter

    }

    private fun setUpSpinnerHandler() {
        binding.categorySpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    subject = binding.categorySpinner.getItemAtPosition(position).toString()
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }
            }
        binding.bankSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                targetBank = binding.bankSpinner.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun moveActivity(
        title: String,
        cntPerWeek: Int,
        show: Boolean,
        targetAccount: String,
        percent: Int,
        subject: String,
        targetBank: String,
        price: Int,
        term: Int,
        email: String?
    ) {
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

    private fun getForkStatus() {
        challengId = intent.getStringExtra("challengeId").toString()
        isFork = !challengId.isNullOrEmpty()
        if (isFork) {getChallengeData(challengId)}
        else {
            setUpCategorySpinner()
            setUpBankSpinner()
            setUpSpinnerHandler()
        }
    }

    private fun getChallengeData(cid: String) {
        db.collection("Challenge").document(cid).get().addOnSuccessListener { result ->
            val title: String? = result["title"] as? String
            val subject: String? = result["subject"] as? String
            val nCntPerWeek: Number? = result["cntPerWeek"] as? Number
            val nTerm: Number? = result["term"] as? Number
            val nPrice: Number? = result["price"] as? Number
            val show: Boolean? = result["show"] as? Boolean

            val iCntPerWeek = nCntPerWeek?.toInt()
            val iTerm = nTerm?.toInt()
            val iPrice = nPrice?.toInt()

            challengeInfo.title = title
            challengeInfo.subject = subject
            if (iCntPerWeek != null) {
                challengeInfo.cntPerWeek = iCntPerWeek
            }
            if (iTerm != null) {
                challengeInfo.term = iTerm
            }
            if (iPrice != null) {
                challengeInfo.price = iPrice
            }
            if (show != null) {
                challengeInfo.show = show
            }
            Log.d("TAG", "getChallengeData: $challengeInfo ")
            showLoadingDialog()
            setUpCategorySpinner()
            setUpBankSpinner()
            setUpSpinnerHandler()
        }
    }

    private fun setForkedValue(categoryPos: Int) {
        binding.categorySpinner.setSelection(categoryPos)
        binding.writeTitle.setText(challengeInfo.title)
        Log.d("TAG", "challenge: ${challengeInfo.cntPerWeek} ")

        once.isChecked=true
        when (challengeInfo.cntPerWeek) {
            1 -> binding.countGroup.check(binding.once.id)
            3 -> binding.countGroup.check(binding.threetimes.id)
            5 -> binding.countGroup.check(binding.fivetimes.id)
            7 -> binding.countGroup.check(binding.seventimes.id)
        }
        oneweek.isChecked = true
        when (challengeInfo.term) {
            1 -> binding.periodGroup.check(binding.oneweek.id)
            2 -> binding.periodGroup.check(binding.twoweek.id)
            3 -> binding.periodGroup.check(binding.threeweek.id)
            4 -> binding.periodGroup.check(binding.fourweek.id)
        }
        ten_thousand.isChecked = true
        when (challengeInfo.price) {
            10000 -> binding.moneyGroup.check(binding.tenThousand.id)
            30000 -> binding.moneyGroup.check(binding.threeThousand.id)
            50000 -> binding.moneyGroup.check(binding.fiftyThousand.id)
            70000 -> binding.moneyGroup.check(binding.seventyThousand.id)
            100000 -> binding.moneyGroup.check(binding.hundredThousand.id)
        }
        when (challengeInfo.show) {
            true -> binding.exposeGroup.check(binding.open.id)
            false -> binding.exposeGroup.check(binding.close.id)
        }
    }
    private fun goBackActivity() {
        finish()
    }

    private fun showLoadingDialog() {
        val dialog = LoadingDialog(this)
        CoroutineScope(Dispatchers.Main).launch {
            dialog.show()
            delay(1000)
            dialog.dismiss()
        }
    }
}