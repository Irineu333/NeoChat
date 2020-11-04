package com.neu.neochat.fragment.inicio

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.neu.neochat.model.Usuario

/**
 * Regras de negócio do fragment InicioFragment, seguindo a arquitetura MVP
 * @param inicioView interface de comunicação com a visualização
 * @author Irineu A. Silva
 */

class InicioPresenterImpl(private val inicioView: InicioView) : InicioPresenter,
    ValueEventListener {

    //usuário logado
    private val currentUser: FirebaseUser?
        get() = FirebaseAuth.getInstance().currentUser

    //referência do firebase database
    private val database: DatabaseReference = Firebase.database.reference

    //override InicioPresenter

    override fun checkAuthentication() {
        if (currentUser == null) {
            //Não está logado, solicitar login
            requireLogin()
        } else {
            //Está logado, solicitar checagem no database
            checkOnDatabase()
        }
    }

    override fun requireLogin() {

        inicioView.setVisibilityProgressBar(View.INVISIBLE)

        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.PhoneBuilder().build()
        )

        inicioView.requireLogin(providers)
    }

    /**
     * Checa o resultado da tentativa de login do usuário,
     * 1. se bem sucedido, pede uma checagem no database
     * 2. se falhou, notifica o usuário visualmente
     */
    override fun checkAttemptLogin(resultCode: Int, data: Intent?) {

        //val response = IdpResponse.fromResultIntent(data)

        if (resultCode == Activity.RESULT_OK) {

            //Sucesso
            checkOnDatabase()

        } else {

            //Falha
            inicioView.showToast("Falha na tentativa de login", Toast.LENGTH_SHORT)

            inicioView.setVisibilityProgressBar(View.INVISIBLE)
            inicioView.setVisibilityTryAgainBtn(View.VISIBLE)
        }
    }

    private val myUserDatabase: DatabaseReference
        get() = database.child(Usuario.CHILD).child(currentUser!!.uid)

    override fun checkOnDatabase() {

        inicioView.setVisibilityProgressBar(View.VISIBLE)

        if (currentUser != null) {

            //requisita o firebase database
            myUserDatabase
                .addListenerForSingleValueEvent(this)
        } else {

            inicioView.showToast("Usuário inválido", Toast.LENGTH_SHORT)
        }
    }


    //membros

    private fun updateDatabase(currentUsuarioObject: Usuario) {

        //atualizar usuário no banco de dados
        currentUser?.let { currentUser ->
            database.child(Usuario.CHILD)
                .child(currentUser.uid)
                .setValue(currentUsuarioObject)
                .addOnSuccessListener {

                    inicioView.showToast("Usuário criado", Toast.LENGTH_SHORT)
                    checkOnDatabase()

                }
                .addOnFailureListener {

                    inicioView.showToast("Usuário não criado", Toast.LENGTH_SHORT)
                    Log.d("InicioPresenterImpl", "updateDatabase, Failure: ${it.message}")
                }
        }

    }

    private fun getCurrentUserObject(): Usuario {

        val newUser = Usuario()

        currentUser?.let { user ->
            with(newUser) {
                name = user.displayName ?: ""
                uid = user.uid //obrigatório
                email = user.email ?: ""
                phone = user.phoneNumber ?: ""
            }
        }
        return newUser
    }

    //override ValueEventListener

    override fun onDataChange(snapshot: DataSnapshot) {

        val currentUserObject = getCurrentUserObject()

        if (snapshot.exists() && snapshot.key == currentUser!!.uid) {

            //Usuário existe no database

            val databaseUser = snapshot.getValue<Usuario>()

            var databaseUserName: String = databaseUser!!.name

            while (databaseUserName.contains("."))
                databaseUserName = databaseUserName.replace(".", "")

            if (databaseUserName.trim().isEmpty() || currentUser!!.displayName?.trim()?.isEmpty() != false) {
                inicioView.setVisibilityConfigBtn(View.VISIBLE)
                inicioView.setVisibilityProgressBar(View.GONE)

                //Solicitar nome de usuário
                inicioView.abrirPerfilBottomSheet()
            } else {

                inicioView.navegarParaContatosFragment()
            }

        } else {

            //Usuário não existe no database

            inicioView.showToast("Criando usuário...", Toast.LENGTH_SHORT)
            updateDatabase(currentUserObject)
        }
    }

    override fun onCancelled(error: DatabaseError) {

        inicioView.setVisibilityProgressBar(View.INVISIBLE)
        inicioView.setVisibilityTryAgainBtn(View.VISIBLE)

        Log.d("InicioPresenterImpl", "onCancelled, ${error.message}")
    }

}