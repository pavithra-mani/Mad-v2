package com.example.mad_v2

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_v2.data.Transaction
import com.example.mad_v2.viewmodel.TransactionViewModel

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var amountInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var dateValue: TextView
    private lateinit var saveButton: Button

    private val categories = listOf(
        "Family", "Bills", "Food", "Transport", "Shopping",
        "Health", "Savings", "Other"
    )

    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        amountInput = findViewById(R.id.amountInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        categorySpinner = findViewById(R.id.categorySpinner)
        dateValue = findViewById(R.id.dateValue)
        saveButton = findViewById(R.id.saveButton)

        categorySpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )

        saveButton.setOnClickListener { saveTransaction() }
    }

    private fun saveTransaction() {
        val amount = amountInput.text.toString().toDoubleOrNull()
        val description = descriptionInput.text.toString()
        val category = categorySpinner.selectedItem.toString()
        val date = dateValue.text.toString()

        if (amount == null || description.isEmpty()) {
            Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()
            return
        }

        val transaction = Transaction(
            title = description,
            amount = amount,
            category = category,
            date = date
        )

        viewModel.saveTransaction(transaction)
        Toast.makeText(this, "Transaction Saved", Toast.LENGTH_SHORT).show()
        setResult(RESULT_OK)
        startActivity(Intent(this, TransactionsActivity::class.java))
        finish()

    }
}
