package com.farmtracker.models

data class Income(
    val id: Long = 0,
    val date: String,
    val product: String,
    val quantity: Double,
    val price: Double,
    val total: Double
)
