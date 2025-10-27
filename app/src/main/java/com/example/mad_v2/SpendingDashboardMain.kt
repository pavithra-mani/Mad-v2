package com.example.mad_v2

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton // <-- Import the FAB class

class SpendingDashboardMain : AppCompatActivity(){
    private lateinit var navHome: LinearLayout
    private lateinit var navStats: LinearLayout

    private lateinit var navSettings: LinearLayout // Added for new bottom bar item
    private lateinit var navProfile: LinearLayout // Added for new bottom bar item
    private lateinit var fabAdd: FloatingActionButton // <-- NEW: Use FloatingActionButton for 'Add'

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_spending_dashboard)
        initializeViews()
        setupBottomNavigation()
    }

    private fun initializeViews() {
        navHome = findViewById(R.id.navHome)
        navStats = findViewById(R.id.navStats)
        fabAdd = findViewById(R.id.fab_add)

        navSettings = findViewById(R.id.navSettings)
        navProfile = findViewById(R.id.navProfile)
    }

    private fun setupBottomNavigation() {
        navHome.setOnClickListener {
            // Already on Home page, no action needed or re-initialization logic
        }

        navStats.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }


        fabAdd.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }

        // 4. Settings
        navSettings.setOnClickListener {
            // Add intent to Settings Activity here
        }

        // 5. Profile
        navProfile.setOnClickListener {
            // Add intent to Profile Activity here
        }
    }
}