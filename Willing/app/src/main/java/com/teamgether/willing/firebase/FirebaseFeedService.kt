package com.teamgether.willing.firebase

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await
import java.lang.Exception

object FirebaseFeedService {
    private const val TAG = "FirebaseFeedService"

    suspend fun getFeeds(): List<DocumentSnapshot> {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("Certification").orderBy("timestamp", Query.Direction.DESCENDING)
                .get().await().documents
        } catch (e: Exception) {
            Log.e(TAG, "Error :: "+ e)
            emptyList()
        }
    }

    suspend fun getFollowFeeds(name : String) : List<DocumentSnapshot> {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("Certification").whereEqualTo("userName",name).orderBy("timestamp", Query.Direction.DESCENDING)
                .get().await().documents
        } catch (e: Exception) {
            Log.e(TAG, "Error :: "+ e)
            emptyList()
        }
    }
}