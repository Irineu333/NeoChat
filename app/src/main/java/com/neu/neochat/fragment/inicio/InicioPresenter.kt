package com.neu.neochat.fragment.inicio

import android.content.Intent
import android.view.View
import com.neu.neochat.model.Usuario

interface InicioPresenter {
    /** Checa se o usuário está logado */
    fun checkAuthentication()

    /** Solicita o login do usuário*/
    fun requireLogin()

    /** Checa o resultado da tentativa de login do usuário*/
    fun checkAttemptLogin(resultCode: Int, data: Intent?)

    /** Checa se o usuário logado existe no database */
    fun checkOnDatabase()
}