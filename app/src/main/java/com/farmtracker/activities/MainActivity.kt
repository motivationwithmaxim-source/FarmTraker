package com.farmtracker.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.farmtracker.R
import com.farmtracker.database.DatabaseHelper
import com.farmtracker.databinding.ActivityMainBinding
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = DatabaseHelper(this)
        setSupportActionBar(binding.toolbar)
        setupButtons()
    }

    override fun onResume() {
        super.onResume()
        updateSummary()
    }

    private fun setupButtons() {
        binding.btnAddExpense.setOnClickListener {
            startActivity(Intent(this, AddExpenseActivity::class.java))
        }
        binding.btnAddIncome.setOnClickListener {
            startActivity(Intent(this, AddIncomeActivity::class.java))
        }
        binding.btnOverview.setOnClickListener {
            startActivity(Intent(this, OverviewActivity::class.java))
        }
        binding.btnExpenseList.setOnClickListener {
            startActivity(Intent(this, ExpenseListActivity::class.java))
        }
        binding.btnIncomeList.setOnClickListener {
            startActivity(Intent(this, IncomeListActivity::class.java))
        }
    }

    private fun updateSummary() {
        val fmt = NumberFormat.getNumberInstance(Locale("sr", "RS")).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        val expenses = db.getTotalExpenses()
        val income = db.getTotalIncome()
        val profit = income - expenses
        binding.tvSummaryExpenses.text = "${fmt.format(expenses)} RSD"
        binding.tvSummaryIncome.text = "${fmt.format(income)} RSD"
        binding.tvSummaryProfit.text = "${fmt.format(profit)} RSD"
        binding.tvSummaryProfit.setTextColor(
            getColor(if (profit >= 0) R.color.green_profit else R.color.red_loss)
        )
    }
}
