package com.neu.neochat.fragment.contatos

import com.neu.neochat.model.Contato
import com.neu.neochat.model.Usuario

interface ContatosView {
    abstract fun showToast(s: String, length: Int)
    fun navegarParaMessages(contato : Contato)
}