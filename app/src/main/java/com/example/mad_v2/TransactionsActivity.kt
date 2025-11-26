package com.example.mad_v2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_v2.data.TransactionDb
import com.example.mad_v2.databinding.ActivityAllTransactionsBinding

class TransactionsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllTransactionsBinding
    private lateinit var adapter: TransactionAdapter
    private val db by lazy { TransactionDb.getDatabase(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAllTransactionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TransactionAdapter(emptyList())
        binding.transactionsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.transactionsRecyclerView.adapter = adapter

        observeTransactions()

        binding.backButton.setOnClickListener { finish() }
    }

    private fun observeTransactions() {
        db.transactionDao().getAllTransactions().observe(this, Observer { list ->
            adapter.updateData(list)
        })
    }
}
