package com.nargyanti.bukukasnusantara.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nargyanti.bukukasnusantara.model.TransactionModel
import com.nargyanti.bukukasnusantara.model.UserModel

class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        private const val DB_NAME = "my_cash_book"
        private const val DB_VERSION = 1

        private const val TABLE_USERS = "users"
        private const val TABLE_TRANSACTIONS = "transactions"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_USERS =
            "CREATE TABLE IF NOT EXISTS $TABLE_USERS ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'username' TEXT NOT NULL UNIQUE, 'password' TEXT NOT NULL);"
        val CREATE_TABLE_TRANSACTIONS =
            "CREATE TABLE IF NOT EXISTS $TABLE_TRANSACTIONS ('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'date' TEXT NOT NULL, 'amount' INTEGER NOT NULL, 'description' TEXT NOT NULL, 'category' TEXT NOT NULL, 'user_id' INTEGER, FOREIGN KEY('user_id') REFERENCES 'Users'('id'));"

        val INSERT_TABLE_USERS = "INSERT INTO $TABLE_USERS ('username', 'password') VALUES ('user', 'user');"
        db?.execSQL(CREATE_TABLE_USERS)
        db?.execSQL(CREATE_TABLE_TRANSACTIONS)

        db?.execSQL(INSERT_TABLE_USERS)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE_USERS = "DROP TABLE IF EXISTS 'users'"
        val DROP_TABLE_TRANSACTIONS = "DROP TABLE IF EXISTS 'transactions'"

        db?.execSQL(DROP_TABLE_USERS)
        db?.execSQL(DROP_TABLE_TRANSACTIONS)
    }

    @SuppressLint("Range")
    fun getUserByUsername(_username: String) : UserModel {
        val user = UserModel()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_USERS WHERE username = '$_username'"
        val cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        user.id = cursor.getInt(cursor.getColumnIndex("id"))
        user.username = cursor.getString(cursor.getColumnIndex("username"))
        user.password = cursor.getString(cursor.getColumnIndex("password"))
        cursor.close()
        return user
    }

    @SuppressLint("Range")
    fun getUserById(_id: Int) : UserModel {
        val user = UserModel()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_USERS WHERE id = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        user.id = cursor.getInt(cursor.getColumnIndex("id"))
        user.username = cursor.getString(cursor.getColumnIndex("username"))
        user.password = cursor.getString(cursor.getColumnIndex("password"))
        cursor.close()
        return user
    }

    fun changePassword(user: UserModel) : Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("id", user.id)
        values.put("password", user.password)
        val _success = db.update(TABLE_USERS, values, "id = ?", arrayOf(user.id.toString())).toLong()
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }

    @SuppressLint("Range")
    fun getAllTransactionsByUserId(_userId: Int): List<TransactionModel> {
        val transactionList = ArrayList<TransactionModel>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_TRANSACTIONS WHERE user_id = $_userId ORDER BY date ASC;"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val transaction = TransactionModel()
                    transaction.id = cursor.getInt(cursor.getColumnIndex("id"))
                    transaction.date = cursor.getString(cursor.getColumnIndex("date"))
                    transaction.amount = cursor.getInt(cursor.getColumnIndex("amount"))
                    transaction.description = cursor.getString(cursor.getColumnIndex("description"))
                    transaction.category = cursor.getString(cursor.getColumnIndex("category"))
                    transaction.user_id = cursor.getInt(cursor.getColumnIndex("user_id"))
                    transactionList.add(transaction)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return transactionList
    }

    @SuppressLint("Range")
    fun getTransactionAmountByUserId(_category: String, _userId: Int): Int {
        val db = writableDatabase
        val selectQuery = "SELECT SUM(amount) as amount FROM $TABLE_TRANSACTIONS WHERE user_id = $_userId AND category = '$_category'"
        val cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        val amount = cursor.getInt(cursor.getColumnIndex("amount"))
        cursor.close()
        return amount
    }

    fun addTransaction(transaction: TransactionModel): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put("date", transaction.date)
        values.put("amount", transaction.amount)
        values.put("description", transaction.description)
        values.put("category", transaction.category)
        values.put("user_id", transaction.user_id)
        val _success = db.insert(TABLE_TRANSACTIONS, null, values)
        db.close()
        return (Integer.parseInt("$_success") != -1)
    }
}