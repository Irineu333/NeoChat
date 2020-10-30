package com.neu.neochat.fragment.messages

import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

interface MessagesPresenter {
    fun startDatabaseListen()
    fun setAdapter(recyclerMessages: RecyclerView)
    fun stopDatabaseListen()
    fun sendMessage(msg : String)
    fun setarDados(contatoName: TextView, status: TextView)
}