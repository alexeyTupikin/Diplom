package com.example.graduationproject.CommonWindows

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.DataPackage.DataMessage.MessageAdapter
import com.example.graduationproject.DataPackage.DataMessage.MessageModel
import com.example.graduationproject.R
import com.example.graduationproject.databinding.ActivityChatWindowBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatWindow : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityChatWindowBinding
    lateinit var recyclerView: RecyclerView
    lateinit var editTextMessage: EditText
    lateinit var buttonSend: Button
    lateinit var adapter: MessageAdapter
    lateinit var refMessage: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatWindowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        editTextMessage = binding.edTextMessage
        buttonSend = binding.buttonSend
        adapter = MessageAdapter()
        recyclerView = binding.rvMessages
        recyclerView.adapter = adapter

        val database = Firebase.database
        val clientName = intent.extras?.getString("client")
        val coachName = intent.extras?.getString("coach")
        if(clientName != null) {
            refMessage = database.getReference("messages").child("${clientName}|${auth.currentUser?.email.toString().substringBefore('@')}")
        } else {
            refMessage = database.getReference("messages").child("${auth.currentUser?.email.toString().substringBefore('@')}|${coachName}")
        }

        buttonSend.setOnClickListener {
            refMessage.child(refMessage.push().key ?: " ")
                .setValue(
                    MessageModel(auth.currentUser?.email,
                    editTextMessage.text.toString())
                )

            editTextMessage.text.clear()
        }
        onChangeListener(refMessage)

    }

    private fun onChangeListener(dRef: DatabaseReference) {
        dRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val listMessages = mutableListOf<MessageModel>()
                for(s in snapshot.children){
                    val message = s.getValue(MessageModel::class.java)
                    if(message != null) {
                        listMessages.add(message)
                    }
                }
                adapter.setListMessages(listMessages)
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.singOutButton) {
            auth.signOut()
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}