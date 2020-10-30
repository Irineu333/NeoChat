package com.neu.neochat.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.ValueEventListener

class Contato (
    var name : String = "",
    var uid: String = "",
    var messagesCount : Int = 0,
    var lastMessage : String = "",
    var noRead : Int = 0
) {
    @Exclude
    var status : String = ""

    @Exclude
    var uid_solicitante: String = ""

    companion object {
        const val CHILD = "contatos"
    }
}
