package com.farmtracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.farmtracker.R
import com.farmtracker.models.Expense
import java.text.NumberFormat
import java.util.Locale

class ExpenseAdapter(
    private val expenses: MutableList<Expense>,
    private val onDelete: (Expense) -> Unit
) : RecyclerView.Adapter<ExpenseAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvIcon: TextView = view.findViewById(R.id.tvIcon)
        val tvType: TextView = view.findViewById(R.id.tvType)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = expenses[position]
        val fmt = NumberFormat.getNumberInstance(Locale("sr", "RS"))
        fmt.minimumFractionDigits = 2
        fmt.maximumFractionDigits = 2
        holder.tvIcon.text = expense.typeIcon()
        holder.tvType.text = expense.type
        holder.tvDate.text = expense.date
        holder.tvDescription.text = expense.description.ifBlank { "-" }
        holder.tvAmount.text = "${fmt.format(expense.amount)} RSD"
        holder.btnDelete.setOnClickListener { onDelete(expense) }
    }

    override fun getItemCount() = expenses.size

    fun removeItem(expense: Expense) {
        val idx = expenses.indexOf(expense)
        if (idx >= 0) {
            expenses.removeAt(idx)
            notifyItemRemoved(idx)
        }
    }
}
