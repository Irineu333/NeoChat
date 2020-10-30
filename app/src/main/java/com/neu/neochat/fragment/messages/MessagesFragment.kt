package com.neu.neochat.fragment.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.neu.neochat.R
import com.neu.neochat.activity.MainActivity
import com.neu.neochat.model.Contato
import kotlinx.android.synthetic.main.fragment_messages.*
import kotlinx.android.synthetic.main.include_send_message.*

/**
 * Fragment responsável por
 * 1. monitorar, listar mensagens dos histórico do usuário
 * 2. enviar mensagens para o histórico do usuário e do destinatário
 * @author Irineu A. Silva
 */

class MessagesFragment : Fragment(), MessagesView, View.OnClickListener {

    private lateinit var messagesPresenter: MessagesPresenter
    private lateinit var contato: Contato

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        contato = Gson().fromJson(
            MessagesFragmentArgs.fromBundle(requireArguments()).contato,
            Contato::class.java
        )

        messagesPresenter = MessagesPresenterImpl(messagesView = this, contato)
        messagesPresenter.startDatabaseListen()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        messagesPresenter.setAdapter(recyclerMessages)
        messagesPresenter.setarDados(contatoName, contatos)

        sendBtn.setOnClickListener(this)
        backBtn.setOnClickListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        messagesPresenter.stopDatabaseListen()
    }

    override fun showToast(s: String, length: Int) {
        Toast.makeText(requireContext(), s, length).show()
    }

    override fun limparCampo() {
        sendMessage.setText("")
    }

    override fun setContatoName(name: String) {
        contatoName.text = name
    }

    override fun setStatus(status: String) {
        this.contatos.text = status
    }

    override fun irParaInicio() {
        recyclerMessages?.let {
            recyclerMessages.layoutManager!!.scrollToPosition(0)
        }
    }

    override fun setError(s: String) {
        sendMessage.error = s
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.sendBtn -> {
                messagesPresenter.sendMessage(sendMessage.text.toString())
            }
            R.id.backBtn -> {
                (requireActivity() as MainActivity).onBackPressed()
            }
        }
    }
}
