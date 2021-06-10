package com.teamgether.willing.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object FirebaseUserService {
    private const val TAG = "FirebaseUserService"

    fun getCurrentUser(): String {
        val firebaseAuth = FirebaseAuth.getInstance()
        return try {
            firebaseAuth.currentUser?.email.toString()
        } catch (e: Exception) {
            Log.e(TAG, "error : " + e)
            ""
        }
    }

    suspend fun getUserInfo(follow: String): List<DocumentSnapshot> {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("User").whereEqualTo("name", follow).get().await().documents
        } catch (e: Exception) {
            Log.e(TAG, "Error : " + e)
            emptyList()
        }
    }

    suspend fun getUserName(email : String) : List<DocumentSnapshot> {
        val db = FirebaseFirestore.getInstance()
        return try {
            db.collection("User").whereEqualTo("email", email).get().await().documents
        } catch (e: Exception) {
            Log.e(TAG, "Error : " + e)
            emptyList()
        }
    }
}