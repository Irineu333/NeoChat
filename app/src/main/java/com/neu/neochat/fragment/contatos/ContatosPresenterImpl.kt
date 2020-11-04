package com.neu.neochat.fragment.contatos

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import com.neu.neochat.adapter.ContatosRecyclerAdapter
import com.neu.neochat.model.Contato
import com.neu.neochat.model.Usuario
import kotlin.collections.HashMap

/**
 * Regras de negócio do ContatosFragment
 */

class ContatosPresenterImpl(private val contatosView: ContatosView) : ContatosPresenter,
    ChildEventListener, ContatosRecyclerAdapter.OnItemCLickListener {

    private var contatosRecyclerAdapter = ContatosRecyclerAdapter(this)

    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val myContatosDatabase = FirebaseDatabase.getInstance().reference
        .child(Contato.CHILD)
        .child(currentUser!!.uid)

    private val usersDatabase = FirebaseDatabase.getInstance().reference
        .child(Usuario.CHILD)

    //override ContatosPresenter

    override fun startDatabseListen() {
        myContatosDatabase.keepSynced(true)
        myContatosDatabase.addChildEventListener(this)
    }

    override fun stopDatabseListen() {
        myContatosDatabase.removeEventListener(this)
    }

    override fun configRecyclerView(recyclerContatos: RecyclerView?) {
        recyclerContatos?.adapter = contatosRecyclerAdapter
    }

    private fun addContato(id: String) {
        val newContatoMap = HashMap<String, Any>()
        newContatoMap["uid"] = id
        myContatosDatabase.child(id).updateChildren(newContatoMap)
    }

    //membro

    override fun getContatoFromArgs(requireArguments: Bundle) {
        var id: String? = null

        if (requireArguments.containsKey("userId"))
            id = ContatosFragmentArgs.fromBundle(requireArguments).userId

        if (!id.isNullOrEmpty()) {
            addContato(id)
            requireArguments.clear()
        }
    }

    //override ChildEventListener

    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        val contato = snapshot.getValue<Contato>()
        contato?.let {

            if (contato.uid.trim().isEmpty())
                contato.uid_solicitante = snapshot.key!!

            contatosRecyclerAdapter.add(it)
            carregarUsuario(it)
        }
    }

    private fun carregarUsuario(contato: Contato) {

        val contatoUid = if(contato.uid.trim().isNotEmpty()) contato.uid else contato.uid_solicitante
        usersDatabase
            .child(contatoUid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var usuario = snapshot.getValue<Usuario>()

                    if (usuario == null)
                        usuario = Usuario(name = "Usuário não existe mais", uid = contatoUid)

                    contatosRecyclerAdapter.alterar(usuario)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }


    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        val contato = snapshot.getValue<Contato>()
        contato?.let {

            if (it.uid.trim().isEmpty())
                it.uid_solicitante = snapshot.key!!

            contatosRecyclerAdapter.alterar(it)
            carregarUsuario(it)

            Log.d("ContatosPresenterImpl", "onChildChanged")
        }

    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        val contato = snapshot.getValue<Contato>()
        contato?.let {

            if (contato.uid.trim().isEmpty())
                contato.uid_solicitante = snapshot.key!!

            contatosRecyclerAdapter.remover(contato)
        }
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
        Log.d("ContatosPresenterImpl", "onChildMoved")
    }

    override fun onCancelled(error: DatabaseError) {
        contatosView.showToast("cancelado: ${error.message}", Toast.LENGTH_SHORT)
    }

    //override ContatosRecyclerAdapter.OnItemCLickListener

    override fun onContatoClick(contato: Contato) {
        contatosView.navegarParaMessages(contato)
    }

    override fun onLongClick() {

    }

    override fun onAddContatoClick(contatoUid: String) {
        val newContatoMap = HashMap<String, Any>()
        newContatoMap["uid"] = contatoUid
        myContatosDatabase.child(contatoUid).updateChildren(newContatoMap)
    }
}