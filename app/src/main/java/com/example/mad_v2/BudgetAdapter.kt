package com.example.mad_v2.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mad_v2.data.Budget
import com.example.mad_v2.databinding.ItemBudgetBinding

class BudgetAdapter(
    private val items: MutableList<Budget>,
    private val onDelete: (Budget) -> Unit,
    private val onEdit: (Budget) -> Unit
) : RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder>() {

    inner class BudgetViewHolder(val binding: ItemBudgetBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = ItemBudgetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val item = items[position]
        holder.binding.apply {
            txtTitle.text = if (item.title.isBlank()) item.category else "${item.title} • ${item.category}"
            txtAmount.text = "₹" + String.format("%.2f", item.amount)
            progressPercentage.max = 100
            progressPercentage.progress = item.percentage
            txtPercent.text = "${item.percentage}%"
            btnUpdate.setOnClickListener { onEdit(item) }
            btnDelete.setOnClickListener { onDelete(item) }
        }
    }

    override fun getItemCount(): Int = items.size

    fun setData(newList: List<Budget>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
