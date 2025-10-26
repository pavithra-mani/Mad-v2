package com.example.mad_v2

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
class StatsActivity : AppCompatActivity() {
    private lateinit var navHome: LinearLayout
    private lateinit var navStats: LinearLayout
    private lateinit var navAdd: LinearLayout
    private lateinit var navSettings: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)
        initializeViews()
        setupBottomNavigation()
    }
    private fun initializeViews() {
        navHome = findViewById(R.id.navHome)
        navStats = findViewById(R.id.navStats)
        navAdd = findViewById(R.id.navAdd)
    }
    private fun setupBottomNavigation() {
        navHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        navStats.setOnClickListener {
            // Already on Stats page
        }
        navAdd.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }
}