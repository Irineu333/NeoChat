package com.neu.neochat.fragment.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.neu.neochat.R
import com.neu.neochat.activity.MainActivity
import com.neu.neochat.adapter.UsersRecyclerAdapter
import com.neu.neochat.model.Usuario
import kotlinx.android.synthetic.main.fragment_users.*

/**
 * Fragment responsável por listar todos os usuário cadastrado
 * no aplicativo e permitir ao usuário adiciona-los a seus contatos
 * @author Irineu A. Silva
 */

class UsersFragment : Fragment(), UsersView, View.OnClickListener, UsersRecyclerAdapter.OnItemClickListener {

    private lateinit var usersPresenter: UsersPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        usersPresenter = UsersPresenterImpl(this)
        usersPresenter.startDatabaseListen()
    }

    var mainActivity : MainActivity? = null
    get()
    {
        if(field==null)
            field = mainActivity()
        return field
    }

    //override Fragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //NavigationOnClickListener
        toolbar.setNavigationOnClickListener{

            mainActivity?.onBackPressed()

        }

        usersPresenter.configRecyclerView(recyclerUsuarios)
    }

    override fun onDestroy() {
        super.onDestroy()
        usersPresenter.stopDatabasetListen()
    }

    //membros
    private fun mainActivity() = requireActivity() as MainActivity

    //override OnClickListener
    override fun onClick(v: View?) {

    }

    //override UsersView
    override fun showToast(s: String, length: Int) {
        Toast.makeText(requireContext(), s, length).show()
    }

    override fun onItemClick(user : Usuario) {
        findNavController().navigate(UsersFragmentDirections
            .actionUsersFragmentToContatosFragment(user.uid))
    }
}