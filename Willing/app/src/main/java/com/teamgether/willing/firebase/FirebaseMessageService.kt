package com.teamgether.willing.firebase

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FCM_MESSAGE"

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        if (p0.notification != null) {
            val body = p0.notification!!.body
            Log.d(TAG, "Notification Body: $body")
        }
    }

}
