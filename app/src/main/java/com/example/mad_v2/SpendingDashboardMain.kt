package com.example.mad_v2

import android.content.Intent // Required for setting up navigation intents
import android.os.Bundle
import android.widget.LinearLayout // Required for navHome and navStats
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mad_v2.data.Transaction
import com.example.mad_v2.data.TransactionDao
import com.example.mad_v2.data.TransactionDb
import com.example.mad_v2.databinding.ActivitySpendingDashboardBinding
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.floatingactionbutton.FloatingActionButton // Required for fabAdd
import kotlinx.coroutines.launch
import com.example.mad_v2.data.BillDao // Import BillDao
import com.example.mad_v2.data.Bill // Import Bill entity

class SpendingDashboardMain : AppCompatActivity() {

    private lateinit var binding: ActivitySpendingDashboardBinding
    private lateinit var transactionDao: TransactionDao
    private lateinit var billDao: BillDao

    // 1. DECLARE NAVIGATION COMPONENTS
    private lateinit var navHome: LinearLayout
    private lateinit var navStats: LinearLayout
    private lateinit var fabAdd: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpendingDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = TransactionDb.getDatabase(this)
        transactionDao = db.transactionDao()
        billDao = db.billDao()

        // 2. CALL NAVIGATION SETUP FUNCTIONS
        initializeViews()
        setupBottomNavigation()

        loadCharts()
    }

    // 3. INITIALIZE NAVIGATION VIEWS (using the function you provided)
    private fun initializeViews() {
        // Find views based on the IDs defined in the XML
        navHome = findViewById(R.id.navHome)
        navStats = findViewById(R.id.navStats)
        fabAdd = findViewById(R.id.fab_add)
    }

    // 4. SETUP NAVIGATION LISTENERS (using the function you provided)
    private fun setupBottomNavigation() {
        // 1. Home (Current Page)
        navHome.setOnClickListener {
            // Already on Home page, no action needed
        }

        // 2. Stats
        navStats.setOnClickListener {
            // NOTE: Ensure StatsActivity is defined in your manifest
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }

        // 3. Add Transaction (FAB)
        fabAdd.setOnClickListener {
            // NOTE: Ensure AddTransactionActivity is defined in your manifest
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }

    // --- Existing Data and Charting Logic below ---

    private fun loadCharts() {
        lifecycleScope.launch {
            // Load Transaction Data for historical spending charts
            val transactionList = transactionDao.getAllTransactionsForStats()

            // Load Bill Data for upcoming bills dashboard element
            val billList = billDao.getAllBillsForDashboard()

            // Only proceed if there is data in either list
            if (transactionList.isEmpty() && billList.isEmpty()) return@launch

            // Historical Spending Charts
            setupTotalSpending(transactionList)
            setupPieChart(transactionList)
            setupBarChart(transactionList)

            // Upcoming Bill Summary
            setupUpcomingBills(billList)
        }
    }

    private fun setupTotalSpending(list: List<Transaction>) {
        val total = list.sumOf { it.amount }
        binding.textTotalAmount.text = "₹${"%.2f".format(total)}"
    }

    private fun setupPieChart(list: List<Transaction>) {
        val grouped = list.groupBy { it.category }
            .mapValues { it.value.sumOf { s -> s.amount } }

        val entries = grouped.map { (category, amount) ->
            PieEntry(amount.toFloat(), category)
        }

        val dataSet = PieDataSet(entries, "")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
        dataSet.valueTextSize = 14f

        val data = PieData(dataSet)

        binding.pieChartCategories.apply {
            this.data = data
            description.isEnabled = false
            centerText = "Categories"
            animateY(1000)
            invalidate()
        }
    }

    private fun setupBarChart(list: List<Transaction>) {
        val grouped = list.groupBy { it.date }
            .mapValues { it.value.sumOf { s -> s.amount } }

        val sorted = grouped.toList().sortedBy { it.first }

        val entries = sorted.mapIndexed { index, pair ->
            BarEntry(index.toFloat(), pair.second.toFloat())
        }

        val labels = sorted.map { it.first }

        val dataSet = BarDataSet(entries, "Daily Transactions")
        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        dataSet.valueTextSize = 14f

        val data = BarData(dataSet)

        binding.barChartTrend.apply {
            this.data = data
            xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            description.isEnabled = false
            animateY(1000)
            invalidate()
        }
    }

    /**
     * Calculates and displays the total amount of upcoming bills and the bill count.
     * Requires TextViews 'textTotalUpcomingBills' and 'textUpcomingBillsCount' in the layout.
     */
    private fun setupUpcomingBills(bills: List<Bill>) {
        val totalUpcomingAmount = bills.sumOf { it.amount }
        val billCount = bills.size

        // Assuming these TextViews exist in your ActivitySpendingDashboardBinding layout:
        binding.textTotalUpcomingBills.text = "₹${"%.2f".format(totalUpcomingAmount)}"
        binding.textUpcomingBillsCount.text = "$billCount Bills Due"
    }
}