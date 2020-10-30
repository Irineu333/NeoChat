package com.neu.neochat.fragment.contatos

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView

interface ContatosPresenter {
    fun startDatabseListen()
    fun stopDatabseListen()
    fun configRecyclerView(recyclerContatos: RecyclerView?)
    fun getContatoFromArgs(requireArguments: Bundle)
}