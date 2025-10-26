package com.example.mad_v2
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
class AllTransactionsActivity : AppCompatActivity() {
    private lateinit var backButton: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_transactions)
        initializeViews()
        setupBackButton()
    }
    private fun initializeViews() {
        backButton = findViewById(R.id.backButton)
    }
    private fun setupBackButton() {
        backButton.setOnClickListener {
            finish() // Goes back to previous screen
        }
    }
}