package com.nargyanti.bukukasnusantara.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nargyanti.bukukasnusantara.model.TransactionModel
import com.nargyanti.bukukasnusantara.model.UserModel
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "bukukasnusantara"
        const val DB_VERSION = 1
        const val TABLE_USERS = "users"
        const val TABLE_TRANSACTIONS = "transactions"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // Create tables and insert initial user
        db?.execSQL(createUsersTableQuery)
        db?.execSQL(createTransactionsTableQuery)
        db?.execSQL(insertInitialUserQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop old tables if they exist
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        // Recreate tables
        onCreate(db)
    }

    fun getUserByUsername(username: String): UserModel? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE username = ?"
        val cursor = db.rawQuery(query, arrayOf(username))
        return extractUserFromCursor(cursor)
    }

    fun getUserById(userId: Int): UserModel? {
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_USERS WHERE id = ?"
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))
        return extractUserFromCursor(cursor)
    }

    @SuppressLint("Range")
    private fun extractUserFromCursor(cursor: Cursor): UserModel? {
        if (cursor.moveToFirst()) {
            val user = UserModel().apply {
                id = cursor.getInt(cursor.getColumnIndex("id"))
                username = cursor.getString(cursor.getColumnIndex("username"))
                password = cursor.getString(cursor.getColumnIndex("password"))
            }
            cursor.close()
            return user
        }
        cursor.close()
        return null
    }

    fun addTransaction(transaction: TransactionModel): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("date", transaction.date)
            put("amount", transaction.amount)
            put("description", transaction.description)
            put("category", transaction.category)
            put("user_id", transaction.user_id)
        }

        val success = db.insert(TABLE_TRANSACTIONS, null, values)
        db.close()
        return success != -1L
    }

    private val createUsersTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_USERS (" +
            "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "'username' TEXT NOT NULL UNIQUE, " +
            "'password' TEXT NOT NULL);"

    private val createTransactionsTableQuery = "CREATE TABLE IF NOT EXISTS $TABLE_TRANSACTIONS (" +
            "'id' INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "'date' TEXT NOT NULL, " +
            "'amount' INTEGER NOT NULL, " +
            "'description' TEXT NOT NULL, " +
            "'category' TEXT NOT NULL, " +
            "'user_id' INTEGER, " +
            "FOREIGN KEY('user_id') REFERENCES 'Users'('id'));"

    private val insertInitialUserQuery = "INSERT INTO $TABLE_USERS ('username', 'password') VALUES ('user', 'user');"
}
