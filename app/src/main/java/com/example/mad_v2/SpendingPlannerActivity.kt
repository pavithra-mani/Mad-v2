package com.example.mad_v2

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mad_v2.databinding.ActivitySpendingPlannerBinding

class SpendingPlannerActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySpendingPlannerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpendingPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Navigation buttons
        binding.btnOpenBudget.setOnClickListener {
            startActivity(Intent(this, BudgetPlannerActivity::class.java))
        }

        binding.btnOpenDebt.setOnClickListener {
            startActivity(Intent(this, DebtTrackerActivity::class.java))
        }

        binding.btnOpenBills.setOnClickListener {
            startActivity(Intent(this, BillReminderActivity::class.java))
        }
    }
}