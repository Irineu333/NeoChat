package com.neu.neochat.fragment.perfil

import android.net.Uri
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.neu.neochat.model.Usuario

class PerfilPresenterImpl(private val perfilView: PerfilView) : PerfilPresenter,
    ValueEventListener {

    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val meyUserDatabase = FirebaseDatabase.getInstance().reference
        .child(Usuario.CHILD)
        .child(currentUser!!.uid)

    private var myUserDatabaseObject: Usuario? = null

    private var myUserDatabaseListen = false

    override fun startDatabaseListen() {
        if (!myUserDatabaseListen) {
            meyUserDatabase.addValueEventListener(this)
            myUserDatabaseListen = true
        }
    }

    override fun alterarNoDatabase(name: String) {

        var limpo = ""
        while (name.contains("."))
            limpo = name.replace(".", "")

        if (limpo.trim().isNotEmpty())
            myUserDatabaseObject?.let { user ->

                if (limpo.trim().length < 3)
                    perfilView.setError("Nome muito pequeno")
                else {

                    user.name = name
                    meyUserDatabase.setValue(user).addOnSuccessListener {
                        perfilView.fecharBottomSheet()
                    }
                }
            }
        else
            perfilView.setError("Nome inválido")
    }

    override fun configEditText(editTextName: TextInputEditText?) {
        editTextName?.addTextChangedListener {
            myUserDatabaseObject?.let {
                if (it.toString() == it.name)
                    perfilView.setAlterarBtnVisibility(View.INVISIBLE)
                else
                    perfilView.setAlterarBtnVisibility(View.VISIBLE)
            }
        }
    }

    override fun stopDatabaseListen() {
        meyUserDatabase.removeEventListener(this)
        myUserDatabaseListen = false
    }

    //override ValueEventListener

    override fun onDataChange(snapshot: DataSnapshot) {
        myUserDatabaseObject = snapshot.getValue<Usuario>()
        myUserDatabaseObject?.let { user ->
            perfilView.carregarFotoPerfil(Uri.parse(user.photoUrl))
            perfilView.setNomeUsuario(user.name)
        }
    }

    override fun onCancelled(error: DatabaseError) {}
}