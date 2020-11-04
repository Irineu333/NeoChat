package com.neu.neochat.fragment.inicio

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.neu.neochat.NeoApplication
import com.neu.neochat.R
import com.neu.neochat.fragment.perfil.PerfilBottomSheet
import kotlinx.android.synthetic.main.fragment_splash.*

/**
 * Fragment responsável por
 * 1. verificar e solicitar o login com firebase auth
 * 2. verificar e criar usuários no firebase realtime database,
 * 3. iniciar o próximo fragment quando tudo estiver okay
 * @author Irineu A. Silva
 */

class InicioFragment : Fragment(), View.OnClickListener, InicioView, PerfilBottomSheet.Callback {

    //variável das regras de negócio
    lateinit var inicioPresenter: InicioPresenter

    /**
     * Código usado para chamar startActivityForResult(...) e receber recuperar o resultado,
     * mantive no fragment pois entendo que está diretamente relacionado com o mesmo
     * @author Irineu A. Silva
     */
    private val requestCodeLogin: Int = 26372

    //variável do fragment perfilBottomSheet, instancia quando chamado
    private var perfilBottomSheet: PerfilBottomSheet? = null
        get() {
            if (field == null)
                field = PerfilBottomSheet(this)
            return field
        }

    //override Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //inicializando as regras de negócio
        inicioPresenter = InicioPresenterImpl(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setando os OnClickListeners
        tryAgainBtn.setOnClickListener(this)
        configBtn.setOnClickListener(this)

        //iniciando checagem de autenticação
        inicioPresenter.checkAuthentication()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        Log.d("InicioFragment", "onActivityResult")

        setVisibilityProgressBar(View.VISIBLE)

        //checar resultado da tentativa de login
        if (requestCode == requestCodeLogin)
            inicioPresenter.checkAttemptLogin(resultCode, data)
    }

    //membros

    private fun getApplication() = (activity?.application as NeoApplication)

    //override InicioView

    override fun showToast(s: String, length: Int) {
        Toast.makeText(requireContext(), s, length).show()
    }

    override fun setVisibilityProgressBar(visibility: Int) {
        this.progressBar.visibility = visibility
    }

    override fun navegarParaContatosFragment() {

        //inicia detector de presença (online/offline)
        getApplication().setarPresenca()

        getApplication().updateToken()

        //ir para ContatosFragment
        findNavController().navigate(
            InicioFragmentDirections.actionSplashFragmentToContatosFragment(null)
        )
    }

    override fun setVisibilityTryAgainBtn(visibility: Int) {
        this.tryAgainBtn.visibility = visibility
    }

    /**
     * Chama a tela de login padrão do FirebaseUI
     * @author Irienu A. Silva
     */
    override fun requireLogin(providers: List<AuthUI.IdpConfig>) {

        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setLogo(R.mipmap.ic_launcher)
                .build(),
            requestCodeLogin
        )
    }

    override fun abrirPerfilBottomSheet() {
        perfilBottomSheet?.show(childFragmentManager, "configurarPerfil")
    }

    override fun setVisibilityConfigBtn(visibility: Int) {
        configBtn.visibility = visibility
        avisoRequiredName.visibility = visibility
    }

    //override OnClicklListener
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tryAgainBtn -> {
                inicioPresenter.requireLogin()
            }
            R.id.configBtn -> {
                abrirPerfilBottomSheet()
            }
        }
    }

    //override PerfilBottomSheet.Callback
    override fun onFinish() {
        setVisibilityConfigBtn(View.GONE)
        setVisibilityProgressBar(View.VISIBLE)
        inicioPresenter.checkOnDatabase()
    }

}