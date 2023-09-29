package com.nargyanti.bukukasnusantara

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.nargyanti.bukukasnusantara.database.DatabaseHelper

class LoginActivity : AppCompatActivity() {
    private lateinit var dbHandler: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername: EditText = findViewById(R.id.et_username)
        val etPassword: EditText = findViewById(R.id.et_password)
        val btnLogin: Button = findViewById(R.id.btn_login)

        dbHandler = DatabaseHelper(this)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()

            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(applicationContext, "Isi username atau password yang masih kosong", Toast.LENGTH_LONG).show()
            } else {
                val result = dbHandler.getUserByUsername(username)

                if (result.password == password) {
                    val moveIntent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        putExtra(MainActivity.EXTRA_USER_ID, result.id)
                    }
                    startActivity(moveIntent)
                } else {
                    Toast.makeText(applicationContext, "Username atau password Anda salah", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
