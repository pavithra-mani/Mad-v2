package com.example.mad_v2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mad_v2.databinding.ActivityBudgetPlannerBinding

class BudgetPlannerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBudgetPlannerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}