package com.neu.neochat

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.neu.neochat.activity.MainActivity
import com.neu.neochat.model.Contato

/**
 * Serviço responsável por receber e gerar as notificações
 * @author Irineu A. Silva
 * 03/11/20
 */

class FCMService : FirebaseMessagingService() {

    private val notificationChannelId = "my_channel_id"

    override fun onMessageReceived(message: RemoteMessage) {

        val data = message.data

        val uid = data["remetente"] ?: return

        val currentUid = FirebaseAuth.getInstance().uid ?: return

        val remetenteDatabase = FirebaseDatabase.getInstance().reference
            .child(Contato.CHILD)
            .child(currentUid)
            .child(uid)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


        remetenteDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val contato = snapshot.getValue<Contato>()
                val bundle = Bundle()
                bundle.putString("contato", Gson().toJson(contato))

                val pendingIntent = NavDeepLinkBuilder(applicationContext)
                    .setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.neo_navigation)
                    .setDestination(R.id.messagesFragment)
                    .setArguments(bundle)
                    .createPendingIntent()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val notificationChannel = NotificationChannel(
                        notificationChannelId,
                        "My Notifications",
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                    notificationChannel.description = "Notification channel"
                    notificationChannel.enableLights(true)
                    notificationChannel.vibrationPattern = longArrayOf(500, 500)
                    notificationChannel.lightColor = Color.BLUE

                    notificationManager.createNotificationChannel(notificationChannel)
                }

                val builder = NotificationCompat.Builder(applicationContext, notificationChannelId)
                builder.setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentTitle(data["title"])
                    .setContentText(data["body"])
                    .setAutoCancel(true)
                    .setVibrate(longArrayOf(500, 500))
                    .setContentIntent(pendingIntent)

                notificationManager.notify(toInt(data["remetente"].toString()), builder.build())
            }

            override fun onCancelled(error: DatabaseError) {}

        })

    }

    private fun toInt(value: String): Int {
        val bytes = value.toByteArray()
        var total = 0
        for (b in bytes) {
            total = b.toInt()
        }
        return total
    }

    override fun onNewToken(token: String) {
        updateToken(token)
        super.onNewToken(token)
    }

    private fun updateToken(token: String) {

        val map = HashMap<String, Any>()
        map["token"] = token
        //myUserDatabase.updateChildren(map)
        Log.d("updateToken", token)

        val shared : SharedPreferences = getSharedPreferences("fcm", Activity.MODE_PRIVATE)
        shared.edit().putString("token", token).apply()
    }
}