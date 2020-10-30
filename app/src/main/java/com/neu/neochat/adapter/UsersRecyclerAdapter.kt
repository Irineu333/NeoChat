package com.neu.neochat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.neu.neochat.R
import com.neu.neochat.model.Usuario

class UsersRecyclerAdapter(private val onItemCickListener: OnItemClickListener) :
    RecyclerView.Adapter<UsersRecyclerAdapter.UserHolder>() {

    private val listaUsuarios = mutableListOf<Usuario>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_recycler, parent, false)
        return UserHolder(inflate)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        val user = listaUsuarios[position]
        holder.setarDados(user.name, user.email, user.phone)

        if (user.status == "online")
            holder.isOnline(true)
        else
            holder.isOnline(false)

        val addContatoBtn = holder.itemView.findViewById<ImageButton>(R.id.addUser)

        addContatoBtn.setOnClickListener {
            onItemCickListener.onItemClick(user)
        }
    }

    override fun getItemCount(): Int {
        return listaUsuarios.size
    }

    class UserHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val userName: TextView = view.findViewById(R.id.contatoName)
        private val contato: TextView = view.findViewById(R.id.contatos)
        private val isOnline: ImageView = view.findViewById(R.id.isOnline)

        fun setarDados(userName: String, email: String, phone: String) {
            val contato = "$email${if (email.isEmpty() || phone.isEmpty()) "" else ","} $phone"
            this.userName.text = userName
            this.contato.text = if (contato.trim().isEmpty()) "Anônimo" else contato
        }

        fun isOnline(isOnline: Boolean) {
            if (isOnline)
                this.isOnline.visibility = View.VISIBLE
            else
                this.isOnline.visibility = View.GONE
        }
    }

    fun add(newUser: Usuario) {
        listaUsuarios.add(newUser)
        notifyItemInserted(listaUsuarios.size - 1)
    }

    fun alterar(newUser: Usuario) {
        for ((index, user) in listaUsuarios.withIndex()) {
            if (user.uid == newUser.uid) {
                listaUsuarios.removeAt(index)
                listaUsuarios.add(index, newUser)
                notifyItemChanged(index)
                break
            }
        }
    }

    fun remover(newUser: Usuario) {
        for ((index, user) in listaUsuarios.withIndex()) {
            if (user.uid == newUser.uid) {
                listaUsuarios.removeAt(index)
                notifyItemRemoved(index)
                break
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(user: Usuario)
    }
}