package com.example.mad_v2.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_v2.data.Bill // UPDATED IMPORT
import com.example.mad_v2.adapters.BillClickListener
import com.example.mad_v2.R
import java.text.NumberFormat
import java.util.Locale

/**
 * Adapter for displaying the list of Bill objects in a RecyclerView.
 * Handles the click events for Modify and Delete buttons.
 */
class BillAdapter(
    private val bills: MutableList<Bill>,
    private val clickListener: BillClickListener
) : RecyclerView.Adapter<BillAdapter.BillViewHolder>() {

    class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textBillName: TextView = itemView.findViewById(R.id.textBillName)
        val textBillAmount: TextView = itemView.findViewById(R.id.textBillAmount)
        val textBillDate: TextView = itemView.findViewById(R.id.textBillDate)
        val btnModify: ImageButton = itemView.findViewById(R.id.btnModifyBill)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDeleteBill)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_bill, parent, false)
        return BillViewHolder(view)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val bill = bills[position]

        // Format amount as currency
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale.US)
        val formattedAmount = try {
            currencyFormat.format(bill.amount)
        } catch (e: Exception) {
            bill.amount.toString() // Fallback if formatting fails
        }

        holder.textBillName.text = bill.name
        holder.textBillAmount.text = "Amount: $formattedAmount"
        holder.textBillDate.text = "Due: ${bill.dueDate}"

        // Set click listeners for buttons
        holder.btnModify.setOnClickListener {
            clickListener.onModifyBill(bill)
        }

        holder.btnDelete.setOnClickListener {
            clickListener.onDeleteBill(bill)
        }
    }

    override fun getItemCount(): Int = bills.size

    /**
     * Updates the list of bills and notifies the adapter of the data change.
     */
    fun updateData(newBills: List<Bill>) {
        bills.clear()
        bills.addAll(newBills)
        notifyDataSetChanged()
    }
}