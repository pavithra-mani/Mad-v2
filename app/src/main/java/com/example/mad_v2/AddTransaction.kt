package com.example.mad_v2

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.mad_v2.data.Transaction
import com.example.mad_v2.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat // NEW
import java.util.Calendar // NEW
import java.util.Locale // NEW

class AddTransactionActivity : AppCompatActivity() {

    private lateinit var amountInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var dateValue: TextView
    private lateinit var saveButton: Button

    // Format for storing the date (matches typical database format)
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    // Variable to hold the selected date string, initialized to today
    private var selectedDate: String = dateFormat.format(Calendar.getInstance().time)

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

        // Initialize date TextView to today's date
        dateValue.text = selectedDate

        categorySpinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            categories
        )

        // Set listener to show DatePickerDialog when the TextView is clicked
        dateValue.setOnClickListener { showDatePickerDialog() }

        saveButton.setOnClickListener { saveTransaction() }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()

        // Set the picker to the currently selected date (useful for editing)
        try {
            calendar.time = dateFormat.parse(selectedDate) ?: Calendar.getInstance().time
        } catch (e: Exception) {
            // Log error or fallback to today's date
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            // Listener for when a date is selected
            { _, selectedYear, selectedMonth, selectedDay ->
                // Update the calendar object with the new date
                calendar.set(selectedYear, selectedMonth, selectedDay)

                // Format the new date and store it
                selectedDate = dateFormat.format(calendar.time)

                // Update the TextView display
                dateValue.text = selectedDate

            },
            year, month, day
        )
        datePickerDialog.show()
    }

    private fun saveTransaction() {
        val amount = amountInput.text.toString().toDoubleOrNull()
        val description = descriptionInput.text.toString()
        val category = categorySpinner.selectedItem.toString()
        // Use the centrally managed 'selectedDate' variable
        val date = selectedDate

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