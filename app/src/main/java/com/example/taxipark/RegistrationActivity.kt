package com.example.taxipark

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taxipark.DatabaseHelper.DbHelepr2

class RegistrationActivity : AppCompatActivity() {

    private lateinit var dbHelper: DbHelepr2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        dbHelper = DbHelepr2(this)

        val username = findViewById<EditText>(R.id.username)
        val email = findViewById<EditText>(R.id.email)
        val phoneNumber = findViewById<EditText>(R.id.phoneNumber)
        val password = findViewById<EditText>(R.id.password)
        val registerButton = findViewById<Button>(R.id.registerButton)

        registerButton.setOnClickListener {
            val userNameText = username.text.toString()
            val emailText = email.text.toString()
            val phoneNumberText = phoneNumber.text.toString()
            val passwordText = password.text.toString()

            if (userNameText.isNotEmpty() && emailText.isNotEmpty() && passwordText.isNotEmpty()) {
                // Insert the user into the database
                val db = dbHelper.writableDatabase
                val query = "INSERT INTO Users (Username, Email, PhoneNumber, Password) VALUES (?, ?, ?, ?)"
                val stmt = db.compileStatement(query)
                stmt.bindString(1, userNameText)
                stmt.bindString(2, emailText)
                stmt.bindString(3, phoneNumberText)
                stmt.bindString(4, passwordText)
                stmt.executeInsert()

                // Сохранение данных пользователя в SharedPreferences
                val sharedPreferences = getSharedPreferences("TaxiParkPrefs", MODE_PRIVATE)
                with(sharedPreferences.edit()) {
                    putString("LoggedInUsername", userNameText) // Make sure this key matches in ProfileActivity
                    putString("LoggedInEmail", emailText) // Make sure this key matches in ProfileActivity
                    putString("LoggedInPhoneNumber", phoneNumberText) // Make sure this key matches in ProfileActivity
                    apply() // Применить изменения
                }

                Toast.makeText(this, "Вы успешно зарегистрировались", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
