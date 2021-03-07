package io.arg.cryptowallet.extensions

import android.content.Context
import android.widget.TextView
import io.arg.cryptowallet.R

object KotlinExtensions {

    fun applyTextViewColorBasedOnBalance(context: Context, textView: TextView, balance: String) {
        val balanceValue = balance.toInt()

        if(balanceValue > 0) {
            textView.setTextColor(context.getColor(R.color.colorGood))
        } else {
            textView.setTextColor(context.getColor(R.color.colorBad))
        }
    }

    fun String.Companion.formatBalanceTitle(context: Context, symbol: String): String = symbol.plus(" ").plus(context.getString(R.string.balance)).plus(" :")

    fun String.Companion.formatBalanceValue(symbol: String, balance: String): String = balance.plus(" ").plus(symbol)
}