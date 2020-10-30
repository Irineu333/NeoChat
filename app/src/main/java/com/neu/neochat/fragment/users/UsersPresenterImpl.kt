package com.neu.neochat.fragment.users

import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.neu.neochat.adapter.UsersRecyclerAdapter
import com.neu.neochat.model.Usuario

/**
 * Regras de negócio
 * @author Irineu A. Silva
 */

class UsersPresenterImpl(private val usersView: UsersView) : UsersPresenter, ChildEventListener {

    private val fbDatabase = Firebase.database.reference.child(Usuario.CHILD)
    private val usersRecyclerAdapter = UsersRecyclerAdapter(usersView as UsersFragment)

    override fun startDatabaseListen() {
        fbDatabase.addChildEventListener(this)
    }

    override fun stopDatabasetListen() {
        fbDatabase.removeEventListener(this)
    }

    override fun configRecyclerView(recyclerUsuarios: RecyclerView?) {
        recyclerUsuarios?.adapter = usersRecyclerAdapter
    }

    //override ChildEventListener

    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        val user = snapshot.getValue<Usuario>()
        user?.let {
            usersRecyclerAdapter.add(user)
        }
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        val user = snapshot.getValue<Usuario>()
        user?.let {
            usersRecyclerAdapter.alterar(user)
        }
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        val user = snapshot.getValue<Usuario>()
        user?.let {
            usersRecyclerAdapter.remover(user)
        }
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

    }

    override fun onCancelled(error: DatabaseError) {

    }
}