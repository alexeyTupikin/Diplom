package com.example.graduationproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.DataApplications.ApplicationsAdapter
import com.example.graduationproject.DataApplications.ApplicationsModel
import com.example.graduationproject.DataChat.ChatAdapter
import com.example.graduationproject.DataChat.ChatModel
import com.example.graduationproject.DataMessage.MessageModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatList : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: ChatAdapter
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_chat_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.rv_chatList)
        adapter = ChatAdapter(this.context)
        recyclerView.adapter = adapter

        val database = Firebase.database
        val refMessage = database.getReference("applications")
        onChangeListener(refMessage)

    }

    private fun onChangeListener(dRef: DatabaseReference) {
        auth = Firebase.auth
        dRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val listChat = mutableListOf<ChatModel>()
                val listMessage = mutableListOf<MessageModel>()
                for(s in snapshot.children){
                    val chat = s.getValue(ApplicationsModel::class.java)
                    if((chat != null) && (chat.coach == auth.currentUser?.email.toString().substringBefore('@'))) {
                        val db = Firebase.database
                        val refMessage = db.getReference("messages") //.child("${chat.userName.toString()}|${auth.currentUser?.email.toString().substringBefore('@')}")
                        refMessage.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val listMessage = mutableListOf<MessageModel>()
                                for(s in snapshot.children) {
                                    val message = s.getValue(MessageModel::class.java)
                                    if((message != null) && (message.userName == chat.userName)) {
                                        listMessage.add(message)
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                            }
                        })
                        var lastMessageText: String? = null
                        if(listMessage.isNotEmpty()) {
                            lastMessageText = listMessage.last().textMessage.toString()
                        }
                        val itemChat = ChatModel(chat.userName, lastMessageText)
                        listChat.add(itemChat)
                    }
                }
                adapter.setListChat(listChat)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

}