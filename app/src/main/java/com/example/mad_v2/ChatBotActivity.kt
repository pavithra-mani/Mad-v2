package com.example.mad_v2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_v2.ChatMessage
import com.example.mad_v2.databinding.ActivityChatbotBinding

class ChatbotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatbotBinding
    private lateinit var adapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ChatAdapter(messages)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = adapter

        binding.btnHi.setOnClickListener {
            sendMessage("Hi")
        }

        binding.sendButton.setOnClickListener {
            val msg = binding.inputMessage.text.toString().trim()
            if (msg.isNotEmpty()) {
                sendMessage(msg)
                binding.inputMessage.text.clear()
                binding.inputMessage.clearFocus()
            }
        }
    }

    private fun sendMessage(text: String) {
        messages.add(ChatMessage(text, isUser = true))
        adapter.notifyItemInserted(messages.size - 1)
        binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)
        botReply(text)
    }

    private fun botReply(userMessage: String) {
        val reply = when (userMessage.lowercase()) {
            "hi" -> "Hi! What can I do for you?"
            else -> "You said: $userMessage"
        }
        messages.add(ChatMessage(reply, isUser = false))
        adapter.notifyItemInserted(messages.size - 1)
        binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)
    }
}