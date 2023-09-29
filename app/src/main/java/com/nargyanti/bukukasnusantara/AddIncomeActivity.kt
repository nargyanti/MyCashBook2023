package com.nargyanti.bukukasnusantara

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nargyanti.bukukasnusantara.database.DatabaseHelper
import com.nargyanti.bukukasnusantara.model.TransactionModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddIncomeActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var etDate: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSave: Button
    private lateinit var btnBack: Button
    private lateinit var dbHandler: DatabaseHelper
    private var userId = 0

    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_income)
        supportActionBar?.title = "Tambah Pemasukan"

        etDate = findViewById(R.id.et_date)
        etAmount = findViewById(R.id.et_amount)
        etDescription = findViewById(R.id.et_description)
        btnSave = findViewById(R.id.btn_save)
        btnBack = findViewById(R.id.btn_back)

        dbHandler = DatabaseHelper(this)
        userId = intent.getIntExtra(MainActivity.EXTRA_USER_ID, 0)

        etDate.setOnClickListener(this)
        btnSave.setOnClickListener(this)
        btnBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.et_date -> showDatePicker()
            R.id.btn_save -> saveTransaction()
            R.id.btn_back -> navigateBackToMainActivity()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                val selectedDate = Calendar.getInstance().apply {
                    set(year, monthOfYear, dayOfMonth)
                }
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                etDate.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun saveTransaction() {
        val date = etDate.text.toString()
        val amount = etAmount.text.toString()
        val description = etDescription.text.toString()

        if (amount.isBlank() || date.isBlank() || description.isBlank()) {
            Toast.makeText(applicationContext, "Isi form yang kosong", Toast.LENGTH_LONG).show()
        } else {
            val transaction = TransactionModel().apply {
                this.date = date
                this.amount = amount.toInt()
                this.description = description
                this.category = "Pemasukan"
                this.user_id = userId
            }

            val success = dbHandler.addTransaction(transaction)

            val message = if (success) "Data transaksi berhasil ditambahkan" else "Ada yang salah saat pengisian"
            Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
        }
    }

    private fun navigateBackToMainActivity() {
        val moveIntent = Intent(this@AddIncomeActivity, MainActivity::class.java).apply {
            putExtra(MainActivity.EXTRA_USER_ID, userId)
        }
        startActivity(moveIntent)
    }
}
