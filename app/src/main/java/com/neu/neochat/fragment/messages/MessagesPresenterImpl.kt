package com.neu.neochat.fragment.messages

import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.neu.neochat.adapter.MessagesRecyclerAdapter
import com.neu.neochat.model.Contato
import com.neu.neochat.model.Message
import com.neu.neochat.model.Usuario

/**
 * Regras de negócio do MessagesFragment
 */

class MessagesPresenterImpl(
    private val messagesView: MessagesView,
    private val contato: Contato,
    private val uidFriend: String = contato.uid
) :
    MessagesPresenter, ChildEventListener {

    private var contatoNameTextView: TextView? = null
    private var statusTextView: TextView? = null

    private var contatoNameString: String = contato.name
    private var statusString: String = ""

    private val messagesRecyclerAdapter = MessagesRecyclerAdapter()

    private var userEventValueListener: ValueEventListener? = null
    private var lidoEventValueListener: ValueEventListener? = null

    private val currentUser = FirebaseAuth.getInstance().currentUser

    private val userFriendDatabase = Firebase.database.reference
        .child(Usuario.CHILD)
        .child(uidFriend)

    private val meyMessageDatabaseOfFriend = Firebase.database.reference
        .child(Contato.CHILD)
        .child(currentUser!!.uid)
        .child(uidFriend)
        .child(Message.CHILD)

    private val myContatoDatabase = Firebase.database.reference
        .child(Contato.CHILD)
        .child(currentUser!!.uid)
        .child(uidFriend)


    private var newUser : Usuario? = null

    override fun startDatabaseListen() {

        meyMessageDatabaseOfFriend.addChildEventListener(this)

        userEventValueListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                newUser = snapshot.getValue<Usuario>()

                newUser?.let {
                    contatoNameString = it.name
                    statusString = it.status

                    if (contatoNameTextView != null)
                        contatoNameTextView!!.text = it.name

                    if (statusTextView != null)
                        statusTextView!!.text = it.status
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        userFriendDatabase.keepSynced(true)
        userFriendDatabase.addValueEventListener(userEventValueListener!!)

        val map = HashMap<String, Any>()
        map["noRead"] = 0

        myContatoDatabase.updateChildren(map)
        lidoEventValueListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                myContatoDatabase.updateChildren(map)
            }

            override fun onCancelled(error: DatabaseError) {}
        }
        myContatoDatabase.addValueEventListener(lidoEventValueListener!!)
    }

    override fun setAdapter(recyclerMessages: RecyclerView) {
        recyclerMessages.adapter = messagesRecyclerAdapter
    }

    override fun stopDatabaseListen() {
        meyMessageDatabaseOfFriend.removeEventListener(this)
        userFriendDatabase.removeEventListener(userEventValueListener!!)
        myContatoDatabase.removeEventListener(lidoEventValueListener!!)
    }

    override fun sendMessage(msg: String) {

        if (msg.trim().isNotEmpty()) {

            if (msg.length <= 300) {
                val newPostRef = meyMessageDatabaseOfFriend.push()
                val message = Message(
                    uid_message = newPostRef.key!! /*key única dessa msg*/,
                    message = msg,
                    uid_remetente = currentUser!!.uid,
                    uid_destino = uidFriend
                )

                messagesRecyclerAdapter.addEspera(message)
                messagesView.limparCampo()

                if (message.uid_remetente != message.uid_destino)
                    enviarParaDestino(message)

                newPostRef.setValue(message).addOnSuccessListener {
                    if (message.uid_remetente == message.uid_destino) {
                        message.uid_remetente = "yourself"
                        enviarParaDestino(message)
                    }
                }
            } else {
                messagesView.setError("Mensagem muito grande")
            }
        }
    }

    override fun setarDados(contatoName: TextView, status: TextView) {
        messagesView.setContatoName(contatoNameString)
        messagesView.setStatus(statusString)
        this.contatoNameTextView = contatoName
        this.statusTextView = status
    }


    private fun enviarParaDestino(message: Message) {

        Log.d("MessagesPresenterImpl", "enviarParaDestino")

        //posta no destino
        val databaseMessageFriend =
            Firebase.database.reference.child(Contato.CHILD).child(uidFriend)
                .child(currentUser!!.uid).child(Message.CHILD)

        val friendPostRef = databaseMessageFriend.push()
        friendPostRef.setValue(message).addOnSuccessListener {

            messagesRecyclerAdapter.removeEspera(message)

            val map = HashMap<String, Any>()
            map["lastMessage"] = message.message

            meyMessageDatabaseOfFriend.parent!!.updateChildren(map).addOnSuccessListener {

                databaseMessageFriend.parent?.keepSynced(true)
                databaseMessageFriend.parent?.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        Log.d("MessagesPresenterImpl", "enviarParaDestino, onDataChange")

                        val contato = snapshot.getValue<Contato>()
                        map["noRead"] = contato!!.noRead + 1
                        map["messagesCount"] = contato.messagesCount + 2
                        databaseMessageFriend.parent!!.updateChildren(map)
                            .addOnFailureListener {
                                messagesView.showToast(
                                    "Erro no envio da mensagem",
                                    Toast.LENGTH_SHORT
                                )
                            }
                            .addOnCompleteListener {
                                if(statusString != "online")
                                {
                                    val notification = HashMap<String, Any>()
                                    notification["message"] = message.message
                                    notification["titulo"] = currentUser.displayName!!
                                    notification["remetente"] = currentUser.uid
                                    notification["token"] = newUser!!.token

                                    val databaseNotifications = FirebaseDatabase.getInstance().reference
                                        .child("notifications")

                                    val newRef = databaseNotifications.push()

                                    notification["uid"] = newRef.key!!

                                    newRef.setValue(notification)
                                }
                            }

                        Log.d("onDataChange", "noRead = ${contato.noRead} para ${map["noRead"]}")
                    }

                    override fun onCancelled(error: DatabaseError) {}

                })
            }
        }
    }

    override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
        val message = snapshot.getValue<Message>()
        message?.let {
            messagesRecyclerAdapter.add(it)
            messagesView.irParaInicio()
        }
    }

    override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
        val message = snapshot.getValue<Message>()
        message?.let {
            messagesRecyclerAdapter.altera(it)
        }
    }

    override fun onChildRemoved(snapshot: DataSnapshot) {
        val message = snapshot.getValue<Message>()
        message?.let {
            messagesRecyclerAdapter.remove(it)
        }
    }

    override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

    override fun onCancelled(error: DatabaseError) {}
}