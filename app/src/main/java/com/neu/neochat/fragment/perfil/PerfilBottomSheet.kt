package com.neu.neochat.fragment.perfil

import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.neu.neochat.R
import kotlinx.android.synthetic.main.fragment_perfil.*

class PerfilBottomSheet(private val callback : Callback) : BottomSheetDialogFragment(), PerfilView, View.OnClickListener {

    private var behavior: BottomSheetBehavior<ViewGroup>? = null
    private lateinit var perfilPresenter: PerfilPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        perfilPresenter = PerfilPresenterImpl(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_perfil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        perfilPresenter.startDatabaseListen()
        perfilPresenter.configEditText(editTextName)

        alterarBtn.setOnClickListener(this)
    }


    //override PerfilView

    override fun carregarFotoPerfil(url: Uri) {
        Glide.with(requireView()).load(url).placeholder(R.drawable.ic_user)
            .into(requireView().findViewById(R.id.fotoPerfil))
    }

    override fun setNomeUsuario(name: String) {
        editTextName.setText(name)
        alterarBtn.visibility = View.INVISIBLE
    }

    override fun setError(erro: String) {
        inputTextName.error = erro
    }

    override fun setAlterarBtnVisibility(visibility: Int) {
        alterarBtn.visibility = visibility
    }

    override fun fecharBottomSheet() {
        callback.onFinish()
        dismiss()
    }

    override fun onStart() {
        super.onStart()
        configBottomSheet()
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("PerfilBottomSheet", "onDestroy")
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)

        perfilPresenter.stopDatabaseListen()
        Log.d("PerfilBottomSheet", "onDismiss")
    }

    //membros
    private fun configBottomSheet() {
        if(behavior==null)
        behavior = BottomSheetBehavior.from(requireViewParent())
        behavior?.skipCollapsed = true
    }

    private fun requireViewParent() = requireView().parent as ViewGroup

    //override View.OnClickListener

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.alterarBtn -> {
                perfilPresenter.alterarNoDatabase(editTextName.text.toString())
            }
        }
    }

    interface Callback{
        fun onFinish()
    }

}