package com.nargyanti.bukukasnusantara.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nargyanti.bukukasnusantara.R
import com.nargyanti.bukukasnusantara.model.TransactionModel

class TransactionAdapter (private val transactionList: List<TransactionModel>) : RecyclerView.Adapter<TransactionAdapter.ListViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: TransactionModel)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvCategory: TextView = itemView.findViewById(R.id.tv_category)
        var ivCategory: ImageView = itemView.findViewById(R.id.iv_category)
        var tvAmount: TextView = itemView.findViewById(R.id.tv_amount)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        var tvDate: TextView = itemView.findViewById(R.id.tv_date)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_row_transaction, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val transaction = transactionList[position]
        if (transaction.category == "Pemasukan") {
            holder.tvCategory.text = "[ + ]"
            holder.ivCategory.setImageResource(R.drawable.pemasukan)
        } else {
            holder.tvCategory.text = "[ - ]"
            holder.ivCategory.setImageResource(R.drawable.pengeluaran)
        }
        holder.tvAmount.text = transaction.amount.toString()
        holder.tvDescription.text = transaction.description
        holder.tvDate.text = transaction.date
    }
}