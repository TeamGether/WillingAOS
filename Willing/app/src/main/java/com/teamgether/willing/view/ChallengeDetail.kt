package com.teamgether.willing.view
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeInfo
import kotlinx.android.synthetic.main.activity_challege_detail.*

class ChallengeDetail : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private var firestore : FirebaseFirestore? = null
    private var uid : String? = null

    private var auth = FirebaseAuth.getInstance()


    val user = auth.currentUser

    var challengeInfo: ChallengeInfo = ChallengeInfo(
         title = " ",
         category = "",
         period = "",
         count = "",
         bank  = "",
         account = "",
         money = "",
         expose = ""

    )

    val title = "title"
    val category = "category"
    val period =  "period"
    val count = "count"
    val bank = "bank"
    val account = "account"
    val money = "money"
    val expose = "expose"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = FirebaseAuth.getInstance().currentUser?.uid
        firestore = FirebaseFirestore.getInstance()
        setContentView(R.layout.activity_challege_detail)

        getUserData()
    }

    fun getUserData() {
        db.collection("Challenge").whereEqualTo("email", user.email).get()
            .addOnSuccessListener { result ->
                val document = result.documents[0]
                Log.d("TAG", "result:${document} ")
                challengeInfo.title = document[title] as String
                challengeInfo.category = document[category] as String
                challengeInfo.period = document[period] as String
                challengeInfo.count = document[count] as String
                challengeInfo.bank = document[bank] as String
                challengeInfo.account = document[account] as String
                challengeInfo.money = document[money] as String


                title_tvd.text = challengeInfo.title
                money_tvd.text = challengeInfo.money
                period_tvd.text = challengeInfo.period + challengeInfo.count
                account_tvd.text = challengeInfo.bank + challengeInfo.account


            }.addOnFailureListener { exception ->
                Log.w("TAG", "Error getting documents: ", exception)
            }
    }

    fun getImage(){

    }


}