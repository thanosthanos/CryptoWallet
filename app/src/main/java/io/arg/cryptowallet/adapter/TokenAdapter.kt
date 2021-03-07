package io.arg.cryptowallet.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.arg.cryptowallet.R
import io.arg.cryptowallet.data.server.model.TokenResult
import io.arg.cryptowallet.extensions.KotlinExtensions.applyTextViewColorBasedOnBalance
import io.arg.cryptowallet.extensions.KotlinExtensions.formatBalanceTitle
import io.arg.cryptowallet.extensions.KotlinExtensions.formatBalanceValue


class TokenAdapter (private val context: Context, private val tokenList: List<TokenResult>) :
    RecyclerView.Adapter<TokenAdapter.TokenViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_token, parent, false)
        return TokenViewHolder(view)
    }

    override fun onBindViewHolder(holder: TokenViewHolder, position: Int) {
        val tokenResult = tokenList[position]

        applyTextViewColorBasedOnBalance(context = context, textView = holder.tokenSymbolTextView, balance = tokenResult.balance)
        applyTextViewColorBasedOnBalance(context = context, textView = holder.balanceTextView, balance = tokenResult.balance)

        holder.tokenSymbolTextView.text = String.formatBalanceTitle(context = context, symbol = tokenResult.symbol)
        holder.balanceTextView.text = String.formatBalanceValue(symbol = tokenResult.symbol, balance = tokenResult.balance)
    }

    override fun getItemCount(): Int = tokenList.size

    class TokenViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tokenSymbolTextView: TextView = itemView.findViewById(R.id.tokenSymbolTextView)
        val balanceTextView: TextView = itemView.findViewById(R.id.balanceTextView)
    }
}
