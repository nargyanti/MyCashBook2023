package com.nargyanti.bukukasnusantara.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nargyanti.bukukasnusantara.R
import com.nargyanti.bukukasnusantara.model.TransactionModel

class TransactionAdapter(private val transactionList: List<TransactionModel>) : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(data: TransactionModel)
    }

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCategory: TextView = itemView.findViewById(R.id.tv_category)
        val ivCategory: ImageView = itemView.findViewById(R.id.iv_category)
        val tvAmount: TextView = itemView.findViewById(R.id.tv_amount)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_row_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun getItemCount(): Int = transactionList.size

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]

        holder.tvCategory.text = if (transaction.category == "Pemasukan") "[ + ]" else "[ - ]"
        holder.ivCategory.setImageResource(if (transaction.category == "Pemasukan") R.drawable.money_in else R.drawable.money_out)

        holder.tvAmount.text = transaction.amount.toString()
        holder.tvDescription.text = transaction.description
        holder.tvDate.text = transaction.date
    }
}
