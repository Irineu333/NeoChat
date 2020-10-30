package com.neu.neochat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.neu.neochat.R
import com.neu.neochat.model.Contato
import com.neu.neochat.model.Usuario

class ContatosRecyclerAdapter(private val onItemCLickListener: OnItemCLickListener) :
    RecyclerView.Adapter<ContatosRecyclerAdapter.ContatoHolder>() {

    private val listaContatos = arrayListOf<Contato>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatoHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contato_recycler, parent, false)

        return ContatoHolder(view)
    }

    override fun onBindViewHolder(holder: ContatoHolder, position: Int) {

        val contato = listaContatos[position]

        holder.setContatoName(contato.name)

        if (contato.uid.trim().isNotEmpty()) {
            holder.setMessageCountVisibility(View.VISIBLE)
            holder.setLastMessage(contato.lastMessage)
            holder.setAddVisibility(View.GONE)
        }
        else {
            holder.setMessageCountVisibility(View.GONE)
            holder.setAddVisibility(View.VISIBLE)
            holder.setLastMessage("Me adicione (${contato.noRead} msgs)")
        }

        holder.setMessageCount(contato.noRead)

        if (contato.status == "online")
            holder.isOnline(true)
        else
            holder.isOnline(false)

        holder.itemView.findViewById<ViewGroup>(R.id.contatoLayout)
            .setOnClickListener {
                if (contato.uid.trim().isNotEmpty())
                    onItemCLickListener.onContatoClick(contato)
            }

        holder.itemView.findViewById<ImageView>(R.id.addContato).setOnClickListener {
            onItemCLickListener.onAddContatoClick(contato.uid_solicitante)
        }
    }

    override fun getItemCount(): Int {
        return listaContatos.size
    }

    fun add(newContato: Contato) {
        listaContatos.add(newContato)
        notifyItemInserted(listaContatos.size - 1)
    }

    fun alterar(newContato: Contato) {

        for ((index, contato) in listaContatos.withIndex()) {

            val contatoUid = if(contato.uid.trim().isNotEmpty()) contato.uid else contato.uid_solicitante
            val newContatoUid = if(newContato.uid.trim().isNotEmpty()) newContato.uid else newContato.uid_solicitante

            if (contatoUid == newContatoUid) {
                listaContatos.removeAt(index)
                listaContatos.add(index, newContato)
                notifyItemChanged(index)
                return
            }
        }
    }

    fun alterar(user: Usuario) {
        for ((index, contato) in listaContatos.withIndex()) {
            val contatoUid = if(contato.uid.trim().isNotEmpty()) contato.uid else contato.uid_solicitante
            if (contatoUid == user.uid) {
                listaContatos[index].name = user.name
                listaContatos[index].status = user.status
                notifyItemChanged(index)
                return
            }
        }
    }

    fun remover(newContato: Contato) {
        for ((index, contato) in listaContatos.withIndex()) {
            val contatoUid = if(contato.uid.trim().isNotEmpty()) contato.uid else contato.uid_solicitante
            if (contatoUid == newContato.uid) {
                listaContatos.removeAt(index)
                notifyItemRemoved(index)
                return
            }
        }
    }

    class ContatoHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val contatoName: TextView = view.findViewById(R.id.contatoName)
        private val lastMessage: TextView = view.findViewById(R.id.contatos)
        private val messageCount: TextView = view.findViewById(R.id.messageCount)
        private val isOnline: ImageView = view.findViewById(R.id.isOnline)
        private val addContato: ImageButton = view.findViewById(R.id.addContato)

        fun setContatoName(name: String) {
            contatoName.text = name
        }

        fun setLastMessage(lastMessage: String) {
            if (lastMessage.trim().isEmpty())
                this.lastMessage.visibility = View.GONE
            else {
                this.lastMessage.visibility = View.VISIBLE
                this.lastMessage.text = lastMessage
            }
        }

        fun setMessageCount(count: Int) {
            val messageCount = "$count não lida"
            this.messageCount.text = messageCount
        }

        fun isOnline(isOnline: Boolean) {
            if (isOnline)
                this.isOnline.visibility = View.VISIBLE
            else
                this.isOnline.visibility = View.GONE
        }

        fun setAddVisibility(visibility: Int) {
            addContato.visibility = visibility
        }

        fun setMessageCountVisibility(visibility: Int) {
            messageCount.visibility = visibility
        }

    }

    interface OnItemCLickListener {
        fun onContatoClick(contato: Contato)
        fun onLongClick()
        fun onAddContatoClick(contatoUid : String)
    }
}