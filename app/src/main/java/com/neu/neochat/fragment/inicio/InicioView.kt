package com.neu.neochat.fragment.inicio

import android.view.View
import com.firebase.ui.auth.AuthUI

interface InicioView {
    fun setVisibilityProgressBar(visibility: Int)
    fun setVisibilityConfigBtn(visibility: Int)
    fun setVisibilityTryAgainBtn(visibility: Int)
    fun showToast(s: String, length: Int)
    fun navegarParaContatosFragment()
    fun abrirPerfilBottomSheet()

    /** Chama a tela de login */
    fun requireLogin(providers: List<AuthUI.IdpConfig>)

}