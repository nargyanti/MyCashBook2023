package com.nargyanti.bukukasnusantara

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.nargyanti.bukukasnusantara.database.DatabaseHelper
import com.nargyanti.bukukasnusantara.model.TransactionModel
import java.util.Calendar

class AddExpenseActivity : AppCompatActivity(), View.OnClickListener  {
    private var dbHandler : DatabaseHelper?= null
    private lateinit var etDate: EditText
    private lateinit var etAmount: EditText
    private lateinit var etDescription: EditText
    private lateinit var btnSave: Button
    private lateinit var btnBack: Button
    private var userId = 0

    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_expense)
        supportActionBar?.title = "Tambah Pengeluaran"

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
            R.id.et_date -> {
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)

                val datePickerDialog = DatePickerDialog(this, {
                        view, year, monthOfYear, dayOfMonth -> val date = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    etDate.setText(date)
                }, year, month, day
                )
                datePickerDialog.show()
            }

            R.id.btn_save -> {
                val date = etDate.text.toString()
                val amount = etAmount.text.toString()
                val description = etDescription.text.toString()

                if(amount == "" || date == "" || description == "") {
                    Toast.makeText(applicationContext, "Isi form yang kosong", Toast.LENGTH_LONG).show()
                } else {
                    var success : Boolean = false
                    val transaction : TransactionModel = TransactionModel()

                    transaction.date = date
                    transaction.amount = Integer.parseInt(amount)
                    transaction.description = description
                    transaction.category = "Pengeluaran"
                    transaction.user_id = userId

                    success = dbHandler?.addTransaction(transaction) as Boolean

                    if (success) {
                        Toast.makeText(applicationContext, "Data transaksi berhasil ditambahkan", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, "Ada yang salah saat pengisian", Toast.LENGTH_LONG).show()
                    }
                }
            }

            R.id.btn_back -> {
                val moveIntent = Intent(this@AddExpenseActivity, MainActivity::class.java)
                moveIntent.putExtra(MainActivity.EXTRA_USER_ID, userId)
                startActivity(moveIntent)
            }
        }
    }
}