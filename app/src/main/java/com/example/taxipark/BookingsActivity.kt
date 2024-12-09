package com.example.taxipark

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taxipark.DatabaseHelper.DbHelepr2
import android.widget.ArrayAdapter
import android.widget.TextView

class BookingsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DbHelepr2
    private lateinit var bookingsListView: ListView
    private lateinit var newBookingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookings_activity)

        dbHelper = DbHelepr2(this)
        bookingsListView = findViewById(R.id.bookingsListView)
        newBookingButton = findViewById(R.id.newBookingButton)

        // Загружаем бронирования для текущего пользователя
        loadBookings()

        // Кнопка для добавления нового бронирования
        newBookingButton.setOnClickListener {
            val intent = Intent(this, NewBookingActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadBookings() {
        val userId = 1 // Получите ID пользователя из текущей сессии
        val bookings = dbHelper.getUserBookings(userId)

        if (bookings.isNotEmpty()) {
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_2, android.R.id.text1, bookings)
            bookingsListView.adapter = adapter
        } else {
            Toast.makeText(this, "Нет бронирований", Toast.LENGTH_SHORT).show()
        }
    }
}
