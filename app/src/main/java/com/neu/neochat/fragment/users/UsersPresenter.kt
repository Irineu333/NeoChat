package com.neu.neochat.fragment.users

import androidx.recyclerview.widget.RecyclerView

interface UsersPresenter {
    fun startDatabaseListen()
    fun configRecyclerView(recyclerUsuarios: RecyclerView?)
    fun stopDatabasetListen()
}