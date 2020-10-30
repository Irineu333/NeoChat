package com.neu.neochat

import android.app.Application
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.neu.neochat.model.Usuario

class NeoApplication : Application() {
    override fun onCreate() {
        //Ativar persistência de dados
        Firebase.database.setPersistenceEnabled(true)
        super.onCreate()
    }

    fun startPresenceDetector() {

        val userRef = Firebase.database.reference.child(Usuario.CHILD)
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        detectarOnline(userRef)
        detectarOffline(userRef)
    }

    private fun detectarOnline(userRef: DatabaseReference) {
        val map = HashMap<String, Any>()
        map["status"] = "online"
        userRef.updateChildren(map).addOnSuccessListener {
            detectarOffline(userRef)
        }
    }

    private fun detectarOffline(userRef: DatabaseReference) {
        val map = HashMap<String, Any>()
        map["status"] = "off"
        userRef.onDisconnect().updateChildren(map).addOnSuccessListener {
            detectarOnline(userRef)
        }
    }
}