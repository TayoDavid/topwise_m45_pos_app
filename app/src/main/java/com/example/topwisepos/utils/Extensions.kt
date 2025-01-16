package com.example.topwisepos.utils

import android.content.Intent
import android.os.Parcelable
import android.text.Editable
import java.text.DecimalFormat
import java.util.Date
import java.util.Locale

val String.toAmount: String
    get() {
        val formatter = DecimalFormat("#,###,##0.00")
        val trimmedStr = this.replace(",", "")
        return "₦${formatter.format(trimmedStr.toFloat())}"
    }

val String.trimSize: String
    get() {
        if (this.length > 18) return this.substring(0, 18)
        return this
    }



val Date.formatted: String
    get() {
        val formatter = DecimalFormat("yyyy-MM-dd:hh:mm:ss")
        return formatter.format(this)
    }

val Editable.validAmount: Boolean
    get() {
        if (isNullOrEmpty()) return false
        val amountInKobo = (this.toString().replace("[^\\d-.]".toRegex(), "")
            .toBigDecimal()).toLong().toBigDecimal()
        val transactionAmt = amountInKobo.toDouble()
        return transactionAmt > 1.0
    }

val Editable.emvAmount: String
    get() {
        if (isNullOrEmpty()) return ""
        val amountInKobo = (this.toString().replace("[^\\d-.]".toRegex(), "")
            .toBigDecimal()).toLong().toBigDecimal()
        val transactionAmt = amountInKobo.toDouble()

        val ss = StringBuilder()
        var ch: Char
        val kl = transactionAmt.toString()

        for (element in kl) {
            ch = element
            if (ch != '.') {
                ss.append(ch)
            }
        }
        val formattedAmt = ss.toString()

        val yy: String = padLeftZeros(formattedAmt, 12)

        val amount = yy.toLong().div(10)
        val nn: String = getFormattedAmount(amount.toString())
        return nn
    }

val Editable.doubleAmount: Double
    get() {
        val amountInKobo = (this.toString().replace("[^\\d-.]".toRegex(), "")
            .toBigDecimal()).toLong().toBigDecimal()
        return amountInKobo.toDouble()
    }

val Double.emvAmount: String
    get() {
        val ss = StringBuilder()
        var ch: Char
        val kl = this.toString()

        for (element in kl) {
            ch = element
            if (ch != '.') {
                ss.append(ch)
            }
        }
        val formattedAmt = ss.toString()

        val yy: String = padLeftZeros(formattedAmt, 12)

        val amount = yy.toLong().div(10)
        val nn: String = getFormattedAmount(amount.toString()).toString()
        return nn
    }

val Double.paddedAmount: String
    get() {
        val longAmount = (this * 100).toLong()
        return padLeftZeros(longAmount.toString(), 12)
    }

fun Intent.putParcelableExtra(name: String, value: Parcelable?): Intent {
    putExtra(name, value as Parcelable)
    return this
}

fun padLeftZeros(inputString: String, length: Int): String {
    if (inputString.length >= length) {
        return inputString
    }
    val sb = StringBuilder()
    while (sb.length < length - inputString.length) {
        sb.append('0')
    }
    sb.append(inputString)
    return sb.toString()
}

fun getFormattedAmount(amount: String): String {
    val result = amount.toDouble()
    return String.format(Locale.US, "%,.2f", result)
}

