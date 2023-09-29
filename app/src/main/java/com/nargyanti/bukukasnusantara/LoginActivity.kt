package com.nargyanti.bukukasnusantara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.nargyanti.bukukasnusantara.database.DatabaseHelper

class LoginActivity : AppCompatActivity() {
    private var dbHandler : DatabaseHelper?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername: EditText = findViewById(R.id.et_username)
        val etPassword: EditText = findViewById(R.id.et_password)
        val btnLogin: Button = findViewById(R.id.btn_login)

        dbHandler = DatabaseHelper(this)

        btnLogin.setOnClickListener{
            var username = etUsername.text.toString()
            var password = etPassword.text.toString()
            val result = dbHandler!!.getUserByUsername(username)
            if(username == "" || password == "") {
                Toast.makeText(applicationContext, "Isi username / password yang masih kosong", Toast.LENGTH_LONG).show()
            } else {
                if(result.password == password) {
                    val moveIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    moveIntent.putExtra(MainActivity.EXTRA_USER_ID, result.id)
                    startActivity(moveIntent)
                } else {
                    Toast.makeText(applicationContext, "Username atau password Anda salah", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}