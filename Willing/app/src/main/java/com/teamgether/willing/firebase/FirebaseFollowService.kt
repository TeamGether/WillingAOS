package com.teamgether.willing.firebase

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseFollowService {
    private const val TAG = "FirebaseFeedService"

    suspend fun getFollow(path: String, userEmail: String, field: String): ArrayList<String> {
        val db = FirebaseFirestore.getInstance()

        return db.collection(path).document(userEmail).get().await()[field] as ArrayList<String>
    }
}