package com.farmtracker.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.farmtracker.database.DatabaseHelper
import com.farmtracker.databinding.ActivityAddExpenseBinding
import com.farmtracker.models.Expense
import java.util.Calendar

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpenseBinding
    private lateinit var db: DatabaseHelper
    private var selectedDate = ""

    private val expenseTypes = listOf(
        "Gorivo", "Materijal", "Đubrivo", "Prskanje", "Servis", "Ostali troškovi"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Dodaj Trošak"
        db = DatabaseHelper(this)
        setupSpinner()
        setupDatePicker()
        setupSaveButton()
        val cal = Calendar.getInstance()
        selectedDate = "%04d-%02d-%02d".format(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )
        binding.btnPickDate.text = selectedDate
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, expenseTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = adapter
    }

    private fun setupDatePicker() {
        binding.btnPickDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(this,
                { _, year, month, day ->
                    selectedDate = "%04d-%02d-%02d".format(year, month + 1, day)
                    binding.btnPickDate.text = selectedDate
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val type = binding.spinnerType.selectedItem.toString()
            val description = binding.etDescription.text.toString().trim()
            val amountStr = binding.etAmount.text.toString().trim()
            val quantityStr = binding.etQuantity.text.toString().trim()
            val pricePerUnitStr = binding.etPricePerUnit.text.toString().trim()
            if (amountStr.isEmpty()) {
                binding.etAmount.error = "Unesite iznos"
                return@setOnClickListener
            }
            val amount = amountStr.replace(",", ".").toDoubleOrNull()
            if (amount == null || amount <= 0) {
                binding.etAmount.error = "Neispravan iznos"
                return@setOnClickListener
            }
            val quantity = quantityStr.replace(",", ".").toDoubleOrNull() ?: 0.0
            val pricePerUnit = pricePerUnitStr.replace(",", ".").toDoubleOrNull() ?: 0.0
            val expense = Expense(
                type = type,
                date = selectedDate,
                description = description,
                amount = amount,
                quantity = quantity,
                pricePerUnit = pricePerUnit
            )
            db.addExpense(expense)
            Toast.makeText(this, "Trošak sačuvan!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }
}
