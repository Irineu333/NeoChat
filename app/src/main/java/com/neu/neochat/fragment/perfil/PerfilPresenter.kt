package com.neu.neochat.fragment.perfil

import com.google.android.material.textfield.TextInputEditText

interface PerfilPresenter {
    fun startDatabaseListen()
    fun alterarNoDatabase(name : String)
    fun configEditText(editTextName: TextInputEditText?)
    fun stopDatabaseListen()
}