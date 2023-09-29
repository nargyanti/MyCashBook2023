package com.nargyanti.bukukasnusantara

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.nargyanti.bukukasnusantara.database.DatabaseHelper
import com.nargyanti.bukukasnusantara.model.UserModel

class SettingsActivity : AppCompatActivity(), View.OnClickListener  {
    private var dbHandler : DatabaseHelper?= null
    private lateinit var etNewPassword: EditText
    private lateinit var etOldPassword: EditText
    private lateinit var btnSave: Button
    private lateinit var btnBack: Button
    private var userId = 0

    companion object {
        const val EXTRA_USER_ID = "extra_user_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        supportActionBar?.title = "Pengaturan"

        etOldPassword = findViewById(R.id.et_old_password)
        etNewPassword = findViewById(R.id.et_new_password)
        btnSave = findViewById(R.id.btn_save)
        btnBack = findViewById(R.id.btn_back)

        dbHandler = DatabaseHelper(this)
        userId = intent.getIntExtra(MainActivity.EXTRA_USER_ID, 0)

        btnSave.setOnClickListener(this)
        btnBack.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_save -> {
                val oldPassword = etOldPassword.text.toString()
                val newPassword = etNewPassword.text.toString()
                val result = dbHandler!!.getUserById(userId)

                if(oldPassword == "" || newPassword == "") {
                    Toast.makeText(applicationContext, "Isi form yang kosong", Toast.LENGTH_LONG).show()
                } else {
                    if(result.password == oldPassword) {
                        var success : Boolean = false
                        val user : UserModel = UserModel()

                        user.id = userId
                        user.password = newPassword

                        success = dbHandler?.changePassword(user) as Boolean

                        if (success) {
                            Toast.makeText(applicationContext, "Password berhasil diubah", Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(applicationContext, "Ada yang salah saat pengisian", Toast.LENGTH_LONG).show()
                        }
                    } else {
                        Toast.makeText(applicationContext, "Password lama Anda salah", Toast.LENGTH_LONG).show()
                    }
                }
            }

            R.id.btn_back -> {
                val moveIntent = Intent(this@SettingsActivity, MainActivity::class.java)
                moveIntent.putExtra(MainActivity.EXTRA_USER_ID, userId)
                startActivity(moveIntent)
            }
        }
    }
}