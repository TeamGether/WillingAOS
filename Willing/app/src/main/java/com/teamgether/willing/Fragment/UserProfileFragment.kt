package com.teamgether.willing.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.teamgether.willing.R
import com.teamgether.willing.model.Following
import com.teamgether.willing.model.UserInfo
import kotlinx.android.synthetic.main.fragment_my_page.*

class MyPageFragment : Fragment() {

    val user: FirebaseUser = FirebaseAuth.getInstance().currentUser
    private var db = FirebaseFirestore.getInstance()
    var userInfo = UserInfo()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_my_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = user.email
        val imageUrl = ""
        val friend = "Following"
        val name = userInfo.name
        getUserData(email)
        getFriendsCount(friend)

    }

    fun getUserData(email: String) {
        db.collection("User").whereEqualTo("email", email).get().addOnSuccessListener { documents ->
            for (document in documents) {
                userInfo.email = user.email
                val userdata = documents.toObjects<UserInfo>()
                userInfo = userdata[0]

                mp_email.setText(userInfo.email)
                mp_nickName.setText(userInfo.name)
            }
        }.addOnFailureListener { exception ->
            Log.w("TAG", "Error getting documents: ", exception)
        }
    }

    fun getFriendsCount(friend: String) {
        db.collection(friend).document(user.email).get().addOnSuccessListener { documents ->
            documents.data?.values?.size
        }.addOnFailureListener { e ->
            Log.w("TAG", "getFriendsCount: ", e)
        }
    }
}