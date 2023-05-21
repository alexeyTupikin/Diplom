package com.example.graduationproject.DataChat

import android.annotation.SuppressLint
import android.app.appsearch.AppSearchManager.SearchContext
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.ChatWindow
import com.example.graduationproject.databinding.ChatItemModelBinding
import com.google.firebase.auth.FirebaseAuth

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
            userNameText.text = chat.userName
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