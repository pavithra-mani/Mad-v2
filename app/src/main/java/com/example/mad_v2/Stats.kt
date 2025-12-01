package com.example.mad_v2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.mad_v2.data.Transaction
import com.example.mad_v2.data.TransactionDb
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.SimpleDateFormat
import java.util.*

class StatsActivity : AppCompatActivity() {
    private lateinit var navHome: LinearLayout
    private lateinit var navStats: LinearLayout
    private lateinit var navAdd: LinearLayout

    private lateinit var totalSpentText: TextView
    private lateinit var avgDailyText: TextView
    private lateinit var aiInsightText: TextView
    private lateinit var lineChart: LineChart
    private lateinit var pieChart: PieChart
    private lateinit var barChart: BarChart

    private lateinit var shoppingAmount: TextView
    private lateinit var foodAmount: TextView
    private lateinit var billsAmount: TextView
    private lateinit var transportAmount: TextView

    private lateinit var shoppingPercent: TextView
    private lateinit var foodPercent: TextView
    private lateinit var billsPercent: TextView
    private lateinit var transportPercent: TextView

    private lateinit var db: TransactionDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        db = TransactionDb.getDatabase(this)
        initializeViews()
        setupBottomNavigation()
        observeTransactions()
    }

    private fun initializeViews() {
        navHome = findViewById(R.id.navHome)
        navStats = findViewById(R.id.navStats)
        navAdd = findViewById(R.id.navAdd)

        totalSpentText = findViewById(R.id.totalSpentText)
        avgDailyText = findViewById(R.id.avgDailyText)
        aiInsightText = findViewById(R.id.aiInsightText)

        lineChart = findViewById(R.id.lineChart)
        pieChart = findViewById(R.id.pieChart)
        barChart = findViewById(R.id.barChart)

        shoppingAmount = findViewById(R.id.shoppingAmount)
        foodAmount = findViewById(R.id.foodAmount)
        billsAmount = findViewById(R.id.billsAmount)
        transportAmount = findViewById(R.id.transportAmount)

        shoppingPercent = findViewById(R.id.shoppingPercent)
        foodPercent = findViewById(R.id.foodPercent)
        billsPercent = findViewById(R.id.billsPercent)
        transportPercent = findViewById(R.id.transportPercent)
    }

    private fun setupBottomNavigation() {
        navHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        navStats.setOnClickListener {
            // Already on Stats page
        }
        navAdd.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            startActivity(intent)
        }
    }

    private fun observeTransactions() {
        db.transactionDao().getAllTransactions().observe(this, Observer { transactions ->
            if (transactions.isEmpty()) {
                showEmptyState()
            } else {
                updateUI(transactions)
            }
        })
    }

    private fun showEmptyState() {
        totalSpentText.text = "₹0"
        avgDailyText.text = "₹0"
        aiInsightText.text = "No transactions yet. Start adding expenses to see insights!"

        lineChart.clear()
        pieChart.clear()
        barChart.clear()

        shoppingAmount.text = "₹0"
        foodAmount.text = "₹0"
        billsAmount.text = "₹0"
        transportAmount.text = "₹0"

        shoppingPercent.text = "0% • 0 transactions"
        foodPercent.text = "0% • 0 transactions"
        billsPercent.text = "0% • 0 transactions"
        transportPercent.text = "0% • 0 transactions"
    }

    private fun updateUI(transactions: List<Transaction>) {
        updateSummaryCards(transactions)
        updateCategoryBreakdown(transactions)
        setupLineChart(transactions)
        setupPieChart(transactions)
        setupBarChart(transactions)
        generateAIInsights(transactions)
    }

    private fun updateSummaryCards(transactions: List<Transaction>) {
        val totalSpent = transactions.sumOf { it.amount }
        totalSpentText.text = "₹${String.format("%.0f", totalSpent)}"

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dates = transactions.mapNotNull {
            try { dateFormat.parse(it.date) } catch (e: Exception) { null }
        }

        if (dates.isNotEmpty()) {
            val minDate = dates.minOrNull()
            val maxDate = dates.maxOrNull()

            if (minDate != null && maxDate != null) {
                val daysDiff = ((maxDate.time - minDate.time) / (1000 * 60 * 60 * 24)).toInt() + 1
                val avgDaily = if (daysDiff > 0) totalSpent / daysDiff else totalSpent
                avgDailyText.text = "₹${String.format("%.0f", avgDaily)}"
            }
        }
    }

    private fun updateCategoryBreakdown(transactions: List<Transaction>) {
        val categoryTotals = transactions.groupBy { it.category }
            .mapValues { entry ->
                Pair(
                    entry.value.sumOf { it.amount },
                    entry.value.size
                )
            }

        val totalSpent = transactions.sumOf { it.amount }

        val shopping = categoryTotals["Shopping"] ?: Pair(0.0, 0)
        val food = categoryTotals["Food"] ?: Pair(0.0, 0)
        val bills = categoryTotals["Bills"] ?: Pair(0.0, 0)
        val transport = categoryTotals["Transport"] ?: Pair(0.0, 0)

        shoppingAmount.text = "₹${String.format("%.0f", shopping.first)}"
        foodAmount.text = "₹${String.format("%.0f", food.first)}"
        billsAmount.text = "₹${String.format("%.0f", bills.first)}"
        transportAmount.text = "₹${String.format("%.0f", transport.first)}"

        val shoppingPct = if (totalSpent > 0) (shopping.first / totalSpent * 100).toInt() else 0
        val foodPct = if (totalSpent > 0) (food.first / totalSpent * 100).toInt() else 0
        val billsPct = if (totalSpent > 0) (bills.first / totalSpent * 100).toInt() else 0
        val transportPct = if (totalSpent > 0) (transport.first / totalSpent * 100).toInt() else 0

        shoppingPercent.text = "$shoppingPct% • ${shopping.second} transactions"
        foodPercent.text = "$foodPct% • ${food.second} transactions"
        billsPercent.text = "$billsPct% • ${bills.second} transactions"
        transportPercent.text = "$transportPct% • ${transport.second} transactions"
    }

    private fun setupLineChart(transactions: List<Transaction>) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val displayFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

        val dailySpending = transactions
            .groupBy { it.date }
            .mapValues { it.value.sumOf { tx -> tx.amount } }
            .toSortedMap()

        if (dailySpending.isEmpty()) {
            lineChart.clear()
            lineChart.setNoDataText("No data available")
            return
        }

        val entries = dailySpending.entries.mapIndexed { index, entry ->
            Entry(index.toFloat(), entry.value.toFloat())
        }

        val dataSet = LineDataSet(entries, "Daily Spending").apply {
            color = Color.parseColor("#4CAF50")
            setCircleColor(Color.parseColor("#4CAF50"))
            lineWidth = 2f
            circleRadius = 4f
            setDrawValues(false)
            mode = LineDataSet.Mode.CUBIC_BEZIER
        }

        lineChart.apply {
            data = LineData(dataSet)
            description.isEnabled = false
            legend.isEnabled = false

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(dailySpending.keys.map {
                    try {
                        displayFormat.format(dateFormat.parse(it) ?: Date())
                    } catch (e: Exception) {
                        it
                    }
                })
                granularity = 1f
                textColor = Color.parseColor("#666666")
                setLabelCount(minOf(dailySpending.size, 7), false)
            }

            axisLeft.apply {
                textColor = Color.parseColor("#666666")
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "₹${value.toInt()}"
                    }
                }
            }

            axisRight.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            invalidate()
        }
    }

    private fun setupPieChart(transactions: List<Transaction>) {
        val categoryTotals = transactions.groupBy { it.category }
            .mapValues { it.value.sumOf { tx -> tx.amount } }
            .filter { it.value > 0 }

        if (categoryTotals.isEmpty()) {
            pieChart.clear()
            pieChart.setNoDataText("No data available")
            return
        }

        val entries = categoryTotals.map {
            PieEntry(it.value.toFloat(), it.key)
        }

        val colors = listOf(
            Color.parseColor("#4CAF50"),
            Color.parseColor("#66BB6A"),
            Color.parseColor("#81C784"),
            Color.parseColor("#A5D6A7"),
            Color.parseColor("#C8E6C9"),
            Color.parseColor("#E8F5E9")
        )

        val dataSet = PieDataSet(entries, "").apply {
            this.colors = colors
            valueTextSize = 12f
            valueTextColor = Color.WHITE
            sliceSpace = 2f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "${value.toInt()}%"
                }
            }
        }

        pieChart.apply {
            data = PieData(dataSet)
            description.isEnabled = false
            setDrawEntryLabels(true)
            setEntryLabelColor(Color.parseColor("#1B5E20"))
            setEntryLabelTextSize(11f)
            legend.isEnabled = true
            legend.textColor = Color.parseColor("#666666")
            setUsePercentValues(true)
            invalidate()
        }
    }

    private fun setupBarChart(transactions: List<Transaction>) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        val weeklySpending = mutableMapOf<Int, Float>()

        transactions.forEach { transaction ->
            try {
                val date = dateFormat.parse(transaction.date)
                if (date != null) {
                    calendar.time = date
                    val week = calendar.get(Calendar.WEEK_OF_YEAR)
                    weeklySpending[week] = (weeklySpending[week] ?: 0f) + transaction.amount.toFloat()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (weeklySpending.isEmpty()) {
            barChart.clear()
            barChart.setNoDataText("No data available")
            return
        }

        val sortedWeeks = weeklySpending.toSortedMap()
        val entries = sortedWeeks.entries.mapIndexed { index, entry ->
            BarEntry(index.toFloat(), entry.value)
        }

        val dataSet = BarDataSet(entries, "Weekly Spending").apply {
            color = Color.parseColor("#4CAF50")
            valueTextSize = 10f
            valueTextColor = Color.parseColor("#666666")
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "₹${value.toInt()}"
                }
            }
        }

        barChart.apply {
            data = BarData(dataSet)
            description.isEnabled = false
            legend.isEnabled = false

            xAxis.apply {
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = IndexAxisValueFormatter(sortedWeeks.keys.map { "Week $it" })
                granularity = 1f
                textColor = Color.parseColor("#666666")
            }

            axisLeft.apply {
                textColor = Color.parseColor("#666666")
                valueFormatter = object : ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return "₹${value.toInt()}"
                    }
                }
            }

            axisRight.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(false)
            invalidate()
        }
    }

    private fun generateAIInsights(transactions: List<Transaction>) {
        val totalSpent = transactions.sumOf { it.amount }
        val categoryTotals = transactions.groupBy { it.category }
            .mapValues { it.value.sumOf { tx -> tx.amount } }

        val topCategory = categoryTotals.maxByOrNull { it.value }

        val insight = if (topCategory != null && totalSpent > 0) {
            val percentage = ((topCategory.value / totalSpent) * 100).toInt()
            "Your ${topCategory.key} expenses account for $percentage% of total spending (₹${String.format("%.0f", topCategory.value)}). Consider reviewing these expenses to optimize your budget."
        } else {
            "Keep tracking your expenses to get personalized insights!"
        }

        aiInsightText.text = insight
    }
}