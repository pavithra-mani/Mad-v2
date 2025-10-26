package com.example.mad_v2

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mad_v2.databinding.ActivityBillReminderBinding

class BillReminderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBillReminderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBillReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}