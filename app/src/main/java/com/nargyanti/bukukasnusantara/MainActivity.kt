package com.nargyanti.bukukasnusantara

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nargyanti.bukukasnusantara.database.DatabaseHelper

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var userId = 0
    private lateinit var dbHandler: DatabaseHelper

    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
    }

    override fun onRestart() {
        super.onRestart()
        showSummaryAmount()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "MyCashBook"

        val btnAddIncome: ImageButton = findViewById(R.id.btn_add_income)
        val btnAddExpense: ImageButton = findViewById(R.id.btn_add_expense)
        val btnCashFlowDetails: ImageButton = findViewById(R.id.btn_cash_flow_details)
        val btnSettings: ImageButton = findViewById(R.id.btn_settings)

        userId = intent.getIntExtra(EXTRA_USER_ID, 0)
        dbHandler = DatabaseHelper(this)

        btnAddIncome.setOnClickListener(this)
        btnAddExpense.setOnClickListener(this)
        btnCashFlowDetails.setOnClickListener(this)
        btnSettings.setOnClickListener(this)

        showSummaryAmount()
    }

    private fun showSummaryAmount() {
        val tvIncomeAmount: TextView = findViewById(R.id.tv_income_amount)
        val tvExpenseAmount: TextView = findViewById(R.id.tv_expense_amount)

        val incomeAmount = dbHandler.getTransactionAmountByUserId("Pemasukan", userId)
        val expenseAmount = dbHandler.getTransactionAmountByUserId("Pengeluaran", userId)
        tvIncomeAmount.text = incomeAmount.toString()
        tvExpenseAmount.text = expenseAmount.toString()
    }

    override fun onClick(v: View?) {
        val destinationActivity: Class<out AppCompatActivity> = when (v?.id) {
            R.id.btn_add_income -> AddIncomeActivity::class.java
            R.id.btn_add_expense -> AddExpenseActivity::class.java
            R.id.btn_cash_flow_details -> CashFlowDetailsActivity::class.java
            R.id.btn_settings -> SettingsActivity::class.java
            else -> return
        }
        val moveIntent = Intent(this, destinationActivity)
        moveIntent.putExtra(EXTRA_USER_ID, userId)
        startActivity(moveIntent)
    }
}
