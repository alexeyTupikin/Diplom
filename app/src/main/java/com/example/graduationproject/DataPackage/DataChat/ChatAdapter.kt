package com.example.graduationproject.DataPackage.DataChat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.CommonWindows.ChatWindow
import com.example.graduationproject.DataPackage.DataUserPersonal.UserModel
import com.example.graduationproject.DataPackage.WorkoutPlan.WorkoutPlanModel
import com.example.graduationproject.databinding.ChatItemModelBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatAdapter(context: Context?): RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    private var listChat = emptyList<ChatModel>()
    private lateinit var auth: FirebaseAuth
    private var contextWindow = context
    class ChatViewHolder(val binding: ChatItemModelBinding): RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ChatItemModelBinding.inflate(inflater, parent, false)
        return ChatViewHolder(binding)
    }
    override fun getItemCount(): Int = listChat.size
    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = listChat[position]
        with(holder.binding) {
            val db = Firebase.database
            val ref = db.getReference("accounts").child(chat.userName.toString())
            ref.get().addOnSuccessListener {
                ref.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val user = snapshot.getValue(UserModel::class.java)
                        if(user != null) {
                            userNameText.text = "${user.name} ${user.lastName}"
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
            }
            lastMessageText.text = chat.lastMessage
            buttonGoChat.setOnClickListener {
                val goChatWindowIntent = Intent(contextWindow, ChatWindow::class.java)
                goChatWindowIntent.putExtra("client", chat.userName.toString())
                startActivity(contextWindow!!, goChatWindowIntent, null)
            }
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    fun setListChat(list: MutableList<ChatModel>) {
        listChat = list
        notifyDataSetChanged()
    }

}