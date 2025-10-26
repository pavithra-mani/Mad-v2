package com.example.mad_v2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_v2.ChatMessage

class ChatAdapter(private val messages: List<ChatMessage>) :
    RecyclerView.Adapter<ChatAdapter.MessageViewHolder>() {

    inner class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageText: TextView = itemView.findViewById(R.id.messageText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_message, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val msg = messages[position]
        holder.messageText.text = msg.text

        val params = holder.messageText.layoutParams as ViewGroup.MarginLayoutParams
        if (msg.isUser) {
            holder.messageText.setBackgroundColor(0xFF9C27B0.toInt())
            params.marginStart = 100
            params.marginEnd = 0
        } else {
            holder.messageText.setBackgroundColor(0xFFCE93D8.toInt())
            params.marginStart = 0
            params.marginEnd = 100
        }
        holder.messageText.layoutParams = params
    }

    override fun getItemCount() = messages.size
}