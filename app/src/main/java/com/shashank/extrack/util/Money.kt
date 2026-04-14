package com.shashank.extrack.util

import java.text.NumberFormat
import java.util.Locale

fun formatINR(amount: Double): String {
    val nf = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    return nf.format(amount)
}