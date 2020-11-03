package com.neu.neochat

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.neu.neochat.model.Usuario

class NeoApplication : Application() {

    private lateinit var shared : SharedPreferences

    override fun onCreate() {
        //Ativar persistência de dados
        Firebase.database.setPersistenceEnabled(true)
        shared = getSharedPreferences("fcm", Activity.MODE_PRIVATE)
        super.onCreate()
    }

    fun startPresenceDetector() {

        FirebaseAuth.getInstance().currentUser?.let { user ->
            val userRef = Firebase.database.reference.child(Usuario.CHILD)
                .child(user.uid)

            detectarOnline(userRef)
            detectarOffline(userRef)
        }

    }

    private fun detectarOnline(userRef: DatabaseReference) {
        val map = HashMap<String, Any>()
        map["status"] = "online"
        userRef.updateChildren(map).addOnCompleteListener {
            detectarOffline(userRef)
        }
    }

    private fun detectarOffline(userRef: DatabaseReference) {
        val map = HashMap<String, Any>()
        map["status"] = "off-line"

        userRef.onDisconnect().updateChildren(map).addOnCompleteListener {
            detectarOnline(userRef)
        }
    }
    private val myUserDatabase: DatabaseReference
        get() = FirebaseDatabase.getInstance().reference
            .child(Usuario.CHILD)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)


    fun updateToken() {
        val map = HashMap<String, Any>()
        map["token"] = shared.getString("token", "")!!
        myUserDatabase.updateChildren(map)
    }
}