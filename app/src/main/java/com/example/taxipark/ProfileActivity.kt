package com.example.taxipark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.taxipark.DatabaseHelper.DbHelepr2

class ProfileActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DbHelepr2
    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneNumberTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_profile)
        databaseHelper = DbHelepr2(this)

        usernameTextView = findViewById(R.id.usernameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView)

        val sharedPreferences = getSharedPreferences("TaxiParkPrefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("LoggedInUsername", "")
        val email = sharedPreferences.getString("LoggedInEmail", "")
        val phoneNumber = sharedPreferences.getString("LoggedInPhoneNumber", "")

        usernameTextView.text = username ?: "Не указано"
        emailTextView.text = email ?: "Не указано"
        phoneNumberTextView.text = phoneNumber ?: "Не указано"

    }

}