package com.farmtracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.farmtracker.R
import com.farmtracker.models.Income
import java.text.NumberFormat
import java.util.Locale

class IncomeAdapter(
    private val incomes: MutableList<Income>,
    private val onDelete: (Income) -> Unit
) : RecyclerView.Adapter<IncomeAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvProduct: TextView = view.findViewById(R.id.tvProduct)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvDetails: TextView = view.findViewById(R.id.tvDetails)
        val tvTotal: TextView = view.findViewById(R.id.tvTotal)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_income, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val income = incomes[position]
        val fmt = NumberFormat.getNumberInstance(Locale("sr", "RS"))
        fmt.minimumFractionDigits = 2
        fmt.maximumFractionDigits = 2
        holder.tvProduct.text = "🌾 ${income.product}"
        holder.tvDate.text = income.date
        holder.tvDetails.text = "${fmt.format(income.quantity)} kom × ${fmt.format(income.price)} RSD"
        holder.tvTotal.text = "${fmt.format(income.total)} RSD"
        holder.btnDelete.setOnClickListener { onDelete(income) }
    }

    override fun getItemCount() = incomes.size

    fun removeItem(income: Income) {
        val idx = incomes.indexOf(income)
        if (idx >= 0) {
            incomes.removeAt(idx)
            notifyItemRemoved(idx)
        }
    }
}
