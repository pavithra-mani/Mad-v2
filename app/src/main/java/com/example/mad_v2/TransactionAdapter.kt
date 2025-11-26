package com.example.mad_v2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_v2.data.Transaction

class TransactionAdapter(private var transactions: List<Transaction>)
    : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.txtTitle)
        val date: TextView = view.findViewById(R.id.txtDate)
        val amount: TextView = view.findViewById(R.id.txtAmount)
        val categoryIcon: TextView = view.findViewById(R.id.txtCategoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactions[position]
        holder.title.text = transaction.title
        holder.date.text = transaction.date
        holder.amount.text = "₹${transaction.amount}"

        // YOU CAN CHANGE BASED ON CATEGORY IF NEEDED
        holder.categoryIcon.text = "💰"
    }

    override fun getItemCount(): Int = transactions.size

    fun updateData(newList: List<Transaction>) {
        transactions = newList
        notifyDataSetChanged()
    }
}
