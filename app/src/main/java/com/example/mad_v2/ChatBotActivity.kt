package com.example.mad_v2

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_v2.databinding.ActivityChatbotBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class ChatbotActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatbotBinding
    private lateinit var adapter: ChatAdapter
    private val messages = mutableListOf<ChatMessage>()
    private val okHttpClient = OkHttpClient()

    // FIXED: Use 10.0.2.2 for Android emulator to reach host machine
    private val BACKEND_URL = "http://10.0.2.2:5001/query"
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatbotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ChatAdapter(messages)
        binding.chatRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.chatRecyclerView.adapter = adapter

        // Welcome message from Bot
        messages.add(
            ChatMessage(
                "Hello! I'm your Financial Advisor. Ask me anything about finance.",
                isUser = false
            )
        )
        adapter.notifyItemInserted(messages.size - 1)


        binding.btnHi.setOnClickListener {
            sendMessage("Hi")
        }

        // Test connection on startup
        testConnection()

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

        // Disable input while waiting for reply
        binding.sendButton.isEnabled = false
        binding.inputMessage.isEnabled = false

        botReply(text)
    }

    private fun addBotMessage(reply: String) {
        messages.add(ChatMessage(reply, isUser = false))
        adapter.notifyItemInserted(messages.size - 1)
        binding.chatRecyclerView.smoothScrollToPosition(messages.size - 1)

        // Re-enable input
        binding.sendButton.isEnabled = true
        binding.inputMessage.isEnabled = true
    }

    private fun botReply(userMessage: String) {
        // Run network call in a coroutine (background thread)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // 1. Prepare JSON request body (escape quotes properly)
                val escapedMessage = userMessage.replace("\"", "\\\"")
                val json = """{"query": "$escapedMessage"}"""
                val body = json.toRequestBody(JSON_MEDIA_TYPE)

                // 2. Build POST request
                val request = Request.Builder()
                    .url(BACKEND_URL)
                    .post(body)
                    .build()

                Log.d("Chatbot", "Sending request to: $BACKEND_URL")
                Log.d("Chatbot", "Request body: $json")

                // 3. Execute request synchronously (in the IO coroutine)
                okHttpClient.newCall(request).execute().use { response ->
                    val botResponse: String
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        Log.d("Chatbot", "Response: $responseBody")

                        // 4. Parse the JSON response {"response": "..."}
                        // Simple parsing, better to use a library like Gson/Moshi for production
                        botResponse = responseBody
                            ?.substringAfter("\"response\":\"")
                            ?.substringBefore("\"}")
                            ?.replace("\\n", "\n")
                            ?.replace("\\\"", "\"")
                            ?: "Error: Empty response body."
                    } else {
                        Log.e(
                            "Chatbot",
                            "Backend call failed: ${response.code} ${response.message}"
                        )
                        botResponse =
                            "Error: Couldn't connect to the financial advisor service. (Code: ${response.code})"
                    }

                    // 5. Switch to Main thread to update UI
                    withContext(Dispatchers.Main) {
                        addBotMessage(botResponse)
                    }
                }
            } catch (e: IOException) {
                // Handle network failure (server offline, no internet, etc.)
                Log.e("Chatbot", "Network error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    addBotMessage("Connection failed. Ensure the Python server is running on port 5001 and reachable at 10.0.2.2")
                }
            } catch (e: Exception) {
                Log.e("Chatbot", "Unexpected error: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    addBotMessage("An unexpected error occurred: ${e.message}")
                }
            }
        }
    }

    private fun testConnection() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val request = Request.Builder()
                    .url("http://10.0.2.2:5001/health")
                    .get()
                    .build()

                Log.d("Chatbot", "Testing connection to server...")

                okHttpClient.newCall(request).execute().use { response ->
                    if (response.isSuccessful) {
                        val body = response.body?.string()
                        Log.d("Chatbot", "✓ Connection successful! Response: $body")
                        withContext(Dispatchers.Main) {
                            addBotMessage("✓ Connected to server successfully!")
                        }
                    } else {
                        Log.e("Chatbot", "Connection test failed: ${response.code}")
                        withContext(Dispatchers.Main) {
                            addBotMessage("⚠ Server returned error code: ${response.code}")
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("Chatbot", "Connection test failed: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    addBotMessage("✗ Cannot reach server. Is Flask running on port 5001?")
                }
            }
        }
    }
}