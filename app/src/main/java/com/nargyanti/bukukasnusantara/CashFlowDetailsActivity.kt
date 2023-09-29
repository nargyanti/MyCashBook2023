package com.nargyanti.bukukasnusantara

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nargyanti.bukukasnusantara.adapter.TransactionAdapter
import com.nargyanti.bukukasnusantara.database.DatabaseHelper
import com.nargyanti.bukukasnusantara.model.TransactionModel

class CashFlowDetailsActivity : AppCompatActivity() {
    private lateinit var rvTransaction: RecyclerView
    private lateinit var dbHandler: DatabaseHelper
    private var userId = 0

    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_flow_details)
        supportActionBar?.title = "Detail Cash Flow"

        rvTransaction = findViewById(R.id.rv_transaction)
        rvTransaction.setHasFixedSize(true)

        userId = intent.getIntExtra(MainActivity.EXTRA_USER_ID, 0)
        dbHandler = DatabaseHelper(this)

        showRecyclerList()
    }

    private fun showRecyclerList() {
        val transactionList: List<TransactionModel> = dbHandler.getAllTransactionsByUserId(userId)
        rvTransaction.layoutManager = LinearLayoutManager(this)
        rvTransaction.adapter = TransactionAdapter(transactionList)
    }
}
