package com.example.mad_v2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chatBotButton = findViewById<Button>(R.id.btnChatBot)
        val budgetPlannerButton = findViewById<Button>(R.id.btnBudgetPlanner)
        val spendingDashboardButton = findViewById<Button>(R.id.btnSpendingDashboard)
        val billReminderButton = findViewById<Button>(R.id.btnBillReminder)
        val debtReminderButton = findViewById<Button>(R.id.btnDebtReminder)

        chatBotButton.setOnClickListener {
            val intent = Intent(this, ChatbotActivity::class.java)
            startActivity(intent)
        }

        budgetPlannerButton.setOnClickListener {
            val intent = Intent(this, BudgetPlannerActivity::class.java)
            startActivity(intent)
        }

        spendingDashboardButton.setOnClickListener {
            val intent = Intent(this, SpendingDashboardMain::class.java)
            startActivity(intent)
        }

        billReminderButton.setOnClickListener {
            val intent = Intent(this, BillReminderActivity::class.java)
            startActivity(intent)
        }

        debtReminderButton.setOnClickListener {
            val intent = Intent(this, DebtTrackerActivity::class.java)
            startActivity(intent)
        }

        enableEdgeToEdge()
    }
}