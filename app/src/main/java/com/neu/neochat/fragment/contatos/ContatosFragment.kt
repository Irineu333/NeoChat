package com.neu.neochat.fragment.contatos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.gson.Gson
import com.neu.neochat.R
import com.neu.neochat.model.Contato
import com.neu.neochat.model.Usuario
import kotlinx.android.synthetic.main.fragment_contatos.*

/**
 * Fragment responsável por monitorar e listar os contatos do usuário
 * @author Irineu A. Silva
 */

class ContatosFragment : Fragment(), View.OnClickListener, ContatosView {

    private lateinit var contatosPresenter: ContatosPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contatosPresenter = ContatosPresenterImpl(this)
        contatosPresenter.startDatabseListen()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_contatos, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contatosPresenter.configRecyclerView(recyclerContatos)
        floatingActionButton.setOnClickListener(this)
        sairDaConta.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        contatosPresenter.stopDatabseListen()
        Log.d("ContatosFragment","onDestroy")
    }

    override fun onResume() {
        super.onResume()

        contatosPresenter.getContatoFromArgs(requireArguments())
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.floatingActionButton -> {
                findNavController().navigate(
                    ContatosFragmentDirections
                        .actionContatosFragmentToUsersFragment()
                )
            }
            R.id.sairDaConta -> {
                AuthUI.getInstance().signOut(requireContext()).addOnCompleteListener {
                    findNavController().navigate(
                        ContatosFragmentDirections
                            .actionContatosFragmentToSplashFragment())
                }
            }
        }
    }

    override fun showToast(s: String, length: Int) {
        Toast.makeText(requireContext(), s, length).show()
    }

    override fun navegarParaMessages(contato: Contato) {
        val json = Gson().toJson(contato)
        findNavController().navigate(
            ContatosFragmentDirections.actionContatosFragmentToMessagesFragment(json)
        )
    }
}


