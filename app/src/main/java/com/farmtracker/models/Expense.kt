package com.farmtracker.models

data class Expense(
    val id: Long = 0,
    val type: String,
    val date: String,
    val description: String,
    val amount: Double,
    val quantity: Double = 0.0,
    val pricePerUnit: Double = 0.0
) {
    fun typeIcon(): String = when (type) {
        "Gorivo"    -> "⛽"
        "Materijal" -> "🧰"
        "Đubrivo"   -> "🌱"
        "Prskanje"  -> "💧"
        "Servis"    -> "🔧"
        else        -> "📋"
    }
}
