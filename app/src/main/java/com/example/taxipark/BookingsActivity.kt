package com.example.taxipark

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taxipark.DatabaseHelper.DbHelepr2

class BookingsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DbHelepr2
    private lateinit var bookingsRecyclerView: RecyclerView
    private lateinit var newBookingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookings_activity)

        dbHelper = DbHelepr2(this)
        bookingsRecyclerView = findViewById(R.id.bookingsRecyclerView)
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
        val sharedPreferences = getSharedPreferences("TaxiParkPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("LoggedInUserId", -1)

        if (userId == -1) {
            Toast.makeText(this, "Пользователь не авторизован", Toast.LENGTH_SHORT).show()
            return
        }

        // Получаем список бронирований текущего пользователя
        val bookings = dbHelper.getAllBookings()

        if (bookings.isNotEmpty()) {
            // Настроим RecyclerView с адаптером
            val adapter = BookingsAdapter(this, bookings)
            bookingsRecyclerView.layoutManager = LinearLayoutManager(this)
            bookingsRecyclerView.adapter = adapter
        } else {
            Toast.makeText(this, "Нет бронирований", Toast.LENGTH_SHORT).show()
        }
    }
}
