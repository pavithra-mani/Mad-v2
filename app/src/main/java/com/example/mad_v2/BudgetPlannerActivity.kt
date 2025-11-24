package com.example.mad_v2

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mad_v2.data.Budget
import com.example.mad_v2.data.BudgetDb
import com.example.mad_v2.databinding.ActivityBudgetPlannerBinding
import com.example.mad_v2.ui.BudgetAdapter
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

class BudgetPlannerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBudgetPlannerBinding
    private lateinit var db: BudgetDb
    private lateinit var adapter: BudgetAdapter

    private val prefs by lazy { getSharedPreferences("budget_prefs", MODE_PRIVATE) }
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

    // fixed categories (Family + generics + Other)
    private val categories = listOf(
        "Family",
        "Bills",
        "Food",
        "Transport",
        "Shopping",
        "Health",
        "Savings",
        "Other"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBudgetPlannerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = BudgetDb.getDatabase(this)

        setupSpinner()
        setupRecycler()
        loadSavedIncomeAndPayday()
        loadItemsAndRefresh()

        binding.btnSetPayday.setOnClickListener { openDatePicker() }
        binding.btnAddItem.setOnClickListener { onAddItemClicked() }

        // When user changes income and leaves field, we persist and recompute percentages
        binding.inputIncome.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                saveIncomeAndRecompute()
            }
        }
    }

    private fun setupSpinner() {
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapterSpinner

        binding.spinnerCategory.setSelection(0)
        binding.spinnerCategory.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) {
                val sel = categories[position]
                binding.inputCustomCategory.visibility = if (sel == "Other") View.VISIBLE else View.GONE
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        })
    }

    private fun setupRecycler() {
        adapter = BudgetAdapter(mutableListOf(), onDelete = { confirmDelete(it) }, onEdit = { showEditDialog(it) })
        binding.recyclerBudgets.layoutManager = LinearLayoutManager(this)
        binding.recyclerBudgets.adapter = adapter
    }

    private fun loadSavedIncomeAndPayday() {
        val income = prefs.getFloat("income", 0f).toDouble()
        if (income > 0.0) binding.inputIncome.setText(String.format("%.2f", income))

        val payday = prefs.getLong("payday", 0L)
        binding.textPayDay.text = if (payday > 0L) dateFormat.format(Date(payday)) else "Not set"
    }

    private fun openDatePicker() {
        val cal = Calendar.getInstance()
        val dp = DatePickerDialog(this,
            { _, year, month, dayOfMonth ->
                val c = Calendar.getInstance()
                c.set(year, month, dayOfMonth)
                prefs.edit().putLong("payday", c.timeInMillis).apply()
                binding.textPayDay.text = dateFormat.format(c.time)
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        dp.show()
    }

    private fun onAddItemClicked() {
        val title = binding.inputTitle.text.toString().trim()
        val amount = binding.inputAmount.text.toString().toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            showAlert("Enter a valid amount")
            return
        }

        val selected = binding.spinnerCategory.selectedItem?.toString() ?: "Other"
        val category = if (selected == "Other") {
            val custom = binding.inputCustomCategory.text.toString().trim()
            if (custom.isBlank()) {
                showAlert("Please enter custom category name")
                return
            } else custom
        } else selected

        lifecycleScope.launch {
            val income = prefs.getFloat("income", 0f).toDouble()
            val pct = if (income > 0.0) ((amount / income) * 100).roundToInt() else 0
            val item = Budget(title = title, amount = amount, category = category, percentage = pct)
            db.budgetDao().insertItem(item)
            clearNewItemFields()
            loadItemsAndRefresh()
        }
    }

    private fun clearNewItemFields() {
        binding.inputTitle.setText("")
        binding.inputAmount.setText("")
        binding.spinnerCategory.setSelection(0)
        binding.inputCustomCategory.setText("")
        binding.inputCustomCategory.visibility = View.GONE
    }

    private fun loadItemsAndRefresh() {
        lifecycleScope.launch {
            val items = db.budgetDao().getAllItems()
            adapter.setData(items)
            val totalAllocated = db.budgetDao().getTotalAllocated() ?: 0.0
            val income = prefs.getFloat("income", 0f).toDouble()
            binding.textTotalAllocated.text = "Total allocated: ₹" + String.format("%.2f", totalAllocated)
            val remaining = (income - totalAllocated).coerceAtLeast(0.0)
            binding.textRemaining.text = "Remaining: ₹" + String.format("%.2f", remaining)
        }
    }

    private fun saveIncomeAndRecompute() {
        val income = binding.inputIncome.text.toString().toDoubleOrNull() ?: 0.0
        prefs.edit().putFloat("income", income.toFloat()).apply()
        recomputePercentages(income)
    }

    private fun recomputePercentages(income: Double) {
        lifecycleScope.launch {
            val items = db.budgetDao().getAllItems()
            items.forEach { itItem ->
                val p = if (income > 0.0) ((itItem.amount / income) * 100).roundToInt() else 0
                db.budgetDao().updateItem(itItem.copy(percentage = p))
            }
            loadItemsAndRefresh()
        }
    }

    private fun confirmDelete(item: Budget) {
        AlertDialog.Builder(this)
            .setTitle("Delete item")
            .setMessage("Delete ${if (item.title.isBlank()) item.category else item.title}?")
            .setPositiveButton("Delete") { _, _ ->
                lifecycleScope.launch {
                    db.budgetDao().deleteItem(item)
                    loadItemsAndRefresh()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditDialog(item: Budget) {
        // Build a small dialog to edit title, amount, category (including Other)
        val dialogLayout = layoutInflater.inflate(android.R.layout.simple_list_item_1, null) // placeholder
        // Simpler: reuse AlertDialog with custom Views built programmatically
        val container = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(40, 24, 40, 0)
        }
        val titleInput = android.widget.EditText(this).apply {
            hint = "Title (optional)"
            setText(item.title)
        }
        val amountInput = android.widget.EditText(this).apply {
            hint = "Amount"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(item.amount.toString())
        }

        val spinner = android.widget.Spinner(this)
        val adapterSpinner = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterSpinner
        val idx = categories.indexOf(item.category).coerceAtLeast(0)
        spinner.setSelection(if (idx >= 0) idx else categories.size - 1)

        val customInput = android.widget.EditText(this).apply {
            hint = "Custom category"
            visibility = if (categories[spinner.selectedItemPosition] == "Other") View.VISIBLE else View.GONE
        }

        spinner.setOnItemSelectedListener(object : android.widget.AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: android.widget.AdapterView<*>, view: View?, position: Int, id: Long) {
                customInput.visibility = if (categories[position] == "Other") View.VISIBLE else View.GONE
            }
            override fun onNothingSelected(parent: android.widget.AdapterView<*>) {}
        })

        container.addView(titleInput)
        container.addView(amountInput)
        container.addView(spinner)
        container.addView(customInput)

        AlertDialog.Builder(this)
            .setTitle("Edit item")
            .setView(container)
            .setPositiveButton("Save") { _, _ ->
                val newTitle = titleInput.text.toString().trim()
                val newAmount = amountInput.text.toString().toDoubleOrNull() ?: item.amount
                val sel = spinner.selectedItem?.toString() ?: "Other"
                val newCategory = if (sel == "Other") {
                    val c = customInput.text.toString().trim()
                    if (c.isBlank()) "Other" else c
                } else sel
                val income = prefs.getFloat("income", 0f).toDouble()
                val newPct = if (income > 0.0) ((newAmount / income) * 100).roundToInt() else 0

                lifecycleScope.launch {
                    db.budgetDao().updateItem(item.copy(title = newTitle, amount = newAmount, category = newCategory, percentage = newPct))
                    loadItemsAndRefresh()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showAlert(msg: String) {
        AlertDialog.Builder(this)
            .setMessage(msg)
            .setPositiveButton("OK", null)
            .show()
    }
}
