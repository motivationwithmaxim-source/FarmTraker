package com.farmtracker.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.farmtracker.models.Expense
import com.farmtracker.models.Income

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "FarmTracker.db"
        const val DATABASE_VERSION = 1
        const val TABLE_EXPENSES = "expenses"
        const val COL_EXP_ID = "id"
        const val COL_EXP_TYPE = "type"
        const val COL_EXP_DATE = "date"
        const val COL_EXP_DESCRIPTION = "description"
        const val COL_EXP_AMOUNT = "amount"
        const val COL_EXP_QUANTITY = "quantity"
        const val COL_EXP_PRICE_PER_UNIT = "price_per_unit"
        const val TABLE_INCOME = "income"
        const val COL_INC_ID = "id"
        const val COL_INC_DATE = "date"
        const val COL_INC_PRODUCT = "product"
        const val COL_INC_QUANTITY = "quantity"
        const val COL_INC_PRICE = "price"
        const val COL_INC_TOTAL = "total"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_EXPENSES (
                $COL_EXP_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_EXP_TYPE TEXT NOT NULL,
                $COL_EXP_DATE TEXT NOT NULL,
                $COL_EXP_DESCRIPTION TEXT,
                $COL_EXP_AMOUNT REAL NOT NULL,
                $COL_EXP_QUANTITY REAL,
                $COL_EXP_PRICE_PER_UNIT REAL
            )""")
        db.execSQL("""
            CREATE TABLE $TABLE_INCOME (
                $COL_INC_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_INC_DATE TEXT NOT NULL,
                $COL_INC_PRODUCT TEXT NOT NULL,
                $COL_INC_QUANTITY REAL NOT NULL,
                $COL_INC_PRICE REAL NOT NULL,
                $COL_INC_TOTAL REAL NOT NULL
            )""")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_EXPENSES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_INCOME")
        onCreate(db)
    }

    fun addExpense(expense: Expense): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_EXP_TYPE, expense.type)
            put(COL_EXP_DATE, expense.date)
            put(COL_EXP_DESCRIPTION, expense.description)
            put(COL_EXP_AMOUNT, expense.amount)
            put(COL_EXP_QUANTITY, expense.quantity)
            put(COL_EXP_PRICE_PER_UNIT, expense.pricePerUnit)
        }
        val id = db.insert(TABLE_EXPENSES, null, values)
        db.close()
        return id
    }

    fun getAllExpenses(): List<Expense> {
        val list = mutableListOf<Expense>()
        val db = readableDatabase
        val cursor = db.query(TABLE_EXPENSES, null, null, null, null, null, "$COL_EXP_DATE DESC")
        if (cursor.moveToFirst()) {
            do {
                list.add(Expense(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_EXP_ID)),
                    type = cursor.getString(cursor.getColumnIndexOrThrow(COL_EXP_TYPE)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COL_EXP_DATE)),
                    description = cursor.getString(cursor.getColumnIndexOrThrow(COL_EXP_DESCRIPTION)) ?: "",
                    amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_EXP_AMOUNT)),
                    quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_EXP_QUANTITY)),
                    pricePerUnit = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_EXP_PRICE_PER_UNIT))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun deleteExpense(id: Long) {
        val db = writableDatabase
        db.delete(TABLE_EXPENSES, "$COL_EXP_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getTotalExpenses(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COL_EXP_AMOUNT) FROM $TABLE_EXPENSES", null)
        var total = 0.0
        if (cursor.moveToFirst()) total = cursor.getDouble(0)
        cursor.close()
        db.close()
        return total
    }

    fun addIncome(income: Income): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COL_INC_DATE, income.date)
            put(COL_INC_PRODUCT, income.product)
            put(COL_INC_QUANTITY, income.quantity)
            put(COL_INC_PRICE, income.price)
            put(COL_INC_TOTAL, income.total)
        }
        val id = db.insert(TABLE_INCOME, null, values)
        db.close()
        return id
    }

    fun getAllIncome(): List<Income> {
        val list = mutableListOf<Income>()
        val db = readableDatabase
        val cursor = db.query(TABLE_INCOME, null, null, null, null, null, "$COL_INC_DATE DESC")
        if (cursor.moveToFirst()) {
            do {
                list.add(Income(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(COL_INC_ID)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COL_INC_DATE)),
                    product = cursor.getString(cursor.getColumnIndexOrThrow(COL_INC_PRODUCT)),
                    quantity = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_INC_QUANTITY)),
                    price = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_INC_PRICE)),
                    total = cursor.getDouble(cursor.getColumnIndexOrThrow(COL_INC_TOTAL))
                ))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    fun deleteIncome(id: Long) {
        val db = writableDatabase
        db.delete(TABLE_INCOME, "$COL_INC_ID = ?", arrayOf(id.toString()))
        db.close()
    }

    fun getTotalIncome(): Double {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT SUM($COL_INC_TOTAL) FROM $TABLE_INCOME", null)
        var total = 0.0
        if (cursor.moveToFirst()) total = cursor.getDouble(0)
        cursor.close()
        db.close()
        return total
    }
}
