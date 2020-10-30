package com.neu.neochat.model

import com.google.firebase.database.Exclude

data class Usuario(
    var name: String = "",
    var uid: String = "",
    var email: String = "",
    var phone: String = "",
    val photoUrl : String = "",
    val status : String = ""
) {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "email" to email,
            "phone" to phone,
        )
    }

    companion object {
        const val CHILD = "users"
    }
}