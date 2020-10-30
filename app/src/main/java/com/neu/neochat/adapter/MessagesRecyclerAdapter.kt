package com.neu.neochat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.neu.neochat.R
import com.neu.neochat.model.Message

class MessagesRecyclerAdapter : RecyclerView.Adapter<MessagesRecyclerAdapter.MessagesHolder>() {

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val listaMessages = arrayListOf<Message>()
    private val listaEspera = arrayListOf<String>()

    class MessagesHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val messageL: TextView = view.findViewById(R.id.message_l)
        private val messageR: TextView = view.findViewById(R.id.message_r)
        private val bolhaL: CardView = view.findViewById(R.id.bolha_l)
        private val bolhaR: CardView = view.findViewById(R.id.bolha_r)
        private val watchIcon: ImageView = view.findViewById(R.id.watchIcon)

        fun setMessageL(message: String) {
            this.messageL.text = message
            bolhaL.visibility = View.VISIBLE
            bolhaR.visibility = View.GONE
        }

        fun setMessageR(message: String) {
            this.messageR.text = message
            bolhaL.visibility = View.GONE
            bolhaR.visibility = View.VISIBLE
        }

        fun setEspera(esperando: Boolean) {
            if (esperando) {
                watchIcon.setImageResource(R.drawable.ic_watch)
            } else {
                watchIcon.setImageResource(R.drawable.ic_done)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessagesHolder {
        val inflate = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message_recycler, parent, false)

        return MessagesHolder(inflate)
    }

    override fun onBindViewHolder(holder: MessagesHolder, position: Int) {

        val message = listaMessages[position]

        if (message.uid_remetente == currentUser!!.uid) {
            holder.setMessageR(message.message)
            holder.setEspera(verificarEspera(message))
        }
        else
            holder.setMessageL(message.message)

    }

    private fun verificarEspera(
        message: Message
    ): Boolean {
        listaEspera.forEach {
            if (message.uid_message == it) {
                return true
            }
        }
        return false
    }

    override fun getItemCount(): Int {
        return listaMessages.size
    }

    fun add(newMessage: Message) {
        listaMessages.add(0, newMessage)
        notifyItemInserted(0)
    }

    fun altera(alteraMessage: Message) {
        for ((index, message) in listaMessages.withIndex()) {
            if (message.uid_message == alteraMessage.uid_message) {
                listaMessages.removeAt(index)
                listaMessages.add(index, alteraMessage)
                notifyItemChanged(index)
                return
            }
        }
    }

    fun remove(removeMessage: Message) {
        for ((index, message) in listaMessages.withIndex()) {
            if (message.uid_message == removeMessage.uid_message) {
                listaMessages.removeAt(index)
                notifyItemRemoved(index)
                return
            }
        }
    }

    fun addEspera(message: Message) {
        listaEspera.add(message.uid_message)
    }

    fun removeEspera(message: Message) {
        for ((index, uid) in listaEspera.withIndex()) {
            if (message.uid_message == uid) {
                listaEspera.removeAt(index)
                break
            }
        }

        for ((index, msg) in listaMessages.withIndex()) {
            if (message.uid_message == msg.uid_message) {
                notifyItemChanged(index)
            }
        }
    }
}