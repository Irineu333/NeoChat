package com.neu.neochat.fragment.perfil

import android.net.Uri

interface PerfilView {
    fun carregarFotoPerfil(url : Uri)
    fun setNomeUsuario(name: String)
    fun setError(erro: String)
    fun setAlterarBtnVisibility(visibility: Int)
    fun fecharBottomSheet()
}