package com.example.taxipark

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taxipark.DatabaseHelper.DbHelepr2

class NewBookingActivity : AppCompatActivity() {

    private lateinit var dbHelper: DbHelepr2
    private lateinit var pickupLocation: EditText
    private lateinit var dropoffLocation: EditText
    private lateinit var submitBookingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_booking_activity)

        dbHelper = DbHelepr2(this)

        pickupLocation = findViewById(R.id.pickupLocation)
        dropoffLocation = findViewById(R.id.dropoffLocation)
        submitBookingButton = findViewById(R.id.submitBookingButton)

        submitBookingButton.setOnClickListener {
            val pickup = pickupLocation.text.toString()
            val dropoff = dropoffLocation.text.toString()

            if (pickup.isNotEmpty() && dropoff.isNotEmpty()) {
                // Создаем бронирование
                val userId = 1 // Получите ID текущего пользователя
                val orderId = 1 // Для упрощения, можно генерировать заказ вручную
                val status = "Confirmed"
                dbHelper.createBooking(orderId, pickup, dropoff, status, userId)

                Toast.makeText(this, "Booking Confirmed", Toast.LENGTH_SHORT).show()
                finish() // Закрываем активность и возвращаемся на экран с бронированиями
            } else {
                Toast.makeText(this, "Please enter both locations", Toast.LENGTH_SHORT).show()
            }
        }
    }
}