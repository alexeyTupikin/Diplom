package com.example.graduationproject.DataPackage.DataMessage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.graduationproject.databinding.MessageItemModelBinding

class MessageAdapter: RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private var listMessages = emptyList<MessageModel>()

    class MessageViewHolder(val binding: MessageItemModelBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MessageItemModelBinding.inflate(inflater, parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listMessages.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val userMessage = listMessages[position]
        with(holder.binding) {
            userName.text = userMessage.userName
            textMessage.text = userMessage.textMessage
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setListMessages(list: MutableList<MessageModel>) {
        listMessages = list
        notifyDataSetChanged()
    }
}