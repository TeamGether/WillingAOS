package com.teamgether.willing.Fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.teamgether.willing.Adapter.ChallengeListAdapter
import com.teamgether.willing.R
import com.teamgether.willing.model.ChallengeList
import kotlinx.android.synthetic.main.fragment_doing_challenge.*

class ProfileChallengeDoingFragment : Fragment() {
    private lateinit var list: ArrayList<ChallengeList>
    private lateinit var challengeListAdapter: ChallengeListAdapter
    private lateinit var activity: Activity
    private var userEmail: String = ""
    private var auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private var db = FirebaseFirestore.getInstance()


    companion object {
        fun newInstance(): ProfileChallengeDoingFragment = ProfileChallengeDoingFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Activity) {
            activity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_doing_challenge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userEmail = UserProfileFragment.uid

        getChallenge()
        refresh_layout.setOnRefreshListener {
            getChallenge()
            challengeListAdapter.notifyDataSetChanged()
            refresh_layout.isRefreshing = false
        }

    }

    private fun getChallenge() {
        val subjectField = "subject"
        val titleField = "title"
        val percentField = "percent"

        db.collection("Challenge").whereEqualTo("uid", userEmail).whereEqualTo("show", true)
            .whereEqualTo("didFinish", false).get()
            .addOnSuccessListener { result ->
                list = arrayListOf()
                for (document in result) {
                    val challenges = ChallengeList()
                    val documentId = document.id
                    val subject = document[subjectField] as String
                    val title = document[titleField] as String
                    val percent = document[percentField] as Number
                    //if문으로 background color 바꿔주기?
                    challenges.challengeId = documentId
                    challenges.subject = subject
                    challenges.title = title
                    challenges.percent = percent
                    list.add(challenges)

                    challengeListAdapter = ChallengeListAdapter(list, activity)
                    challengeListAdapter.notifyDataSetChanged()
                    mp_challenge_list!!.adapter = challengeListAdapter
                }
            }
    }
}