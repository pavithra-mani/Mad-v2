package com.example.mad_v2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var navHome: LinearLayout
    private lateinit var navStats: LinearLayout
    private lateinit var navAdd: LinearLayout
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        initializeViews()
        setupBottomNavigation()
        setupSaveButton()
    }

    private fun initializeViews() {
        navHome = findViewById(R.id.navHome)
        navStats = findViewById(R.id.navStats)
        navAdd = findViewById(R.id.navAdd)
        saveButton = findViewById(R.id.saveButton)
    }

    private fun setupBottomNavigation() {
        navHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        navStats.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }

        navAdd.setOnClickListener {
            // Already on Add page
        }
    }

    private fun setupSaveButton() {
        saveButton.setOnClickListener {
            Toast.makeText(this, "Transaction Saved!", Toast.LENGTH_SHORT).show()

            // Navigate back to home
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}


