package com.neu.neochat.fragment.messages

interface MessagesView {
    fun showToast(s: String, length: Int)
    fun limparCampo()
    fun setContatoName(name: String)
    fun setStatus(status: String)
    fun irParaInicio()
    fun setError(s: String)
}