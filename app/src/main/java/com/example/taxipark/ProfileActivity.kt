package com.example.taxipark

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class ProfileActivity : AppCompatActivity() {

    private lateinit var usernameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var phoneNumberTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Установите разметку для активности профиля
        setContentView(R.layout.activity_profile)

        // Получите ссылки на TextView из разметки
        usernameTextView = findViewById(R.id.usernameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        phoneNumberTextView = findViewById(R.id.phoneNumberTextView)

        // Здесь вы можете получить данные пользователя из базы данных или SharedPreferences
        // Например:
        val sharedPreferences = getSharedPreferences("TaxiParkPrefs", MODE_PRIVATE)
        val username = sharedPreferences.getString("LoggedInUsername", "")
        val email = sharedPreferences.getString("LoggedInEmail", "")
        val phoneNumber = sharedPreferences.getString("LoggedInPhoneNumber", "")

        // Установите данные в TextView
        usernameTextView.text = username
        emailTextView.text = email
        phoneNumberTextView.text = phoneNumber
    }
}