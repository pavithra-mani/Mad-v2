package com.example.mad_v2
import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SpendingDashboardMain : AppCompatActivity(){
    private lateinit var navHome: LinearLayout
    private lateinit var navStats: LinearLayout
    private lateinit var navAdd: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_dashboard)
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
            // Already on Home page
        }
        navStats.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java) // Changed from Stats
            startActivity(intent)
        }
        navAdd.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java) // Changed from AddTransaction
            startActivity(intent)
        }
    }
}