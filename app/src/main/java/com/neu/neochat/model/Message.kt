package com.neu.neochat.model

class Message(
    var uid_message : String = "",
    var message: String = "",
    var uid_remetente: String = "",
    var uid_destino: String = "",
    var lida : Boolean = false,
    var data: String = "",
    var horas: String = "",
)

{
    companion object
    {
        const val CHILD = "messages"
    }
}
