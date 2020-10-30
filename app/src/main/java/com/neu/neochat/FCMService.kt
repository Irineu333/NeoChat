package com.neu.neochat

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.neu.neochat.activity.MainActivity

class FCMService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {

        val data = message.data

        if(data["sender"] == null)
            return
    }
    override fun onNewToken(p: String) {
        super.onNewToken(p)
    }
}