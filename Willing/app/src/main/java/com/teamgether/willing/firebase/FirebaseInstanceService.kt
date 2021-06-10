package com.teamgether.willing.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService


class FirebaseInstanceService : FirebaseMessagingService() {
    private val TAG = "FCM_ID"

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        val refreshedToken = FirebaseMessaging.getInstance().token
        Log.d(TAG, "FirebaseInstanceId Refreshed token: $refreshedToken")
    }

}