package com.example.taxipark

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taxipark.DatabaseHelper.DbHelepr2

class AuthActivity : AppCompatActivity() {

    private lateinit var dbHelper: DbHelepr2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        dbHelper = DbHelepr2(this)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val userNameText = username.text.toString()
            val passwordText = password.text.toString()

            if (userNameText.isNotEmpty() && passwordText.isNotEmpty()) {
                val isValidUser = dbHelper.getUserByUsernameAndPassword(userNameText, passwordText)

                if (isValidUser) {
                    val userId = dbHelper.getUserIdByUsername(userNameText)
                    val sharedPreferences = getSharedPreferences("TaxiParkPrefs", MODE_PRIVATE)
                    sharedPreferences.edit().putInt("LoggedInUserId", userId).apply()

                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    finish() // Переход к основному экрану
                } else {
                    Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
