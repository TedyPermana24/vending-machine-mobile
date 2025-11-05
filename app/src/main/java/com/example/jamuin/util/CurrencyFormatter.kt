package com.example.jamuin.util

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {
    private val indonesianLocale = Locale("id", "ID")
    private val currencyFormat = NumberFormat.getCurrencyInstance(indonesianLocale)
    
    fun format(amount: Int): String {
        return currencyFormat.format(amount)
    }
    
    fun formatSimple(amount: Int): String {
        val formatter = NumberFormat.getNumberInstance(indonesianLocale)
        return "Rp ${formatter.format(amount)}"
    }
}
