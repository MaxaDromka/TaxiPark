package com.example.taxipark

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Button
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.example.taxipark.DatabaseHelper.DbHelepr2

class NewBookingActivity : AppCompatActivity() {

    private lateinit var dbHelper: DbHelepr2
    private lateinit var pickupLocation: EditText
    private lateinit var dropoffLocation: EditText
    private lateinit var submitBookingButton: Button
    private lateinit var timePicker: TimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_booking_activity)

        dbHelper = DbHelepr2(this)

        pickupLocation = findViewById(R.id.pickupLocation)
        dropoffLocation = findViewById(R.id.dropoffLocation)
        submitBookingButton = findViewById(R.id.submitBookingButton)
        timePicker = findViewById(R.id.timePicker)

        submitBookingButton.setOnClickListener {
            val pickup = pickupLocation.text.toString()
            val dropoff = dropoffLocation.text.toString()

            // Validate addresses
            if (validateAddress(pickup) && validateAddress(dropoff)) {
                // Get current user ID from SharedPreferences
                val sharedPreferences = getSharedPreferences("TaxiParkPrefs", MODE_PRIVATE)
                val userId = sharedPreferences.getInt("LoggedInUserId", -1)

                if (userId != -1) {
                    // Create booking with UserID
                    val orderId = 1  // In a real application, OrderID should be dynamic
                    val status = "Подтвержден"

                    // Get selected time from TimePicker
                    val hour = timePicker.hour
                    val minute = timePicker.minute



                    // Format the selected time as a string (e.g., "14:30")
                    val selectedTime = String.format("%02d:%02d", hour, minute)

                    dbHelper.createBooking(userId, orderId, pickup, dropoff, status)

                    // Notify user of successful booking with selected time
                    Toast.makeText(this, "Бронирование создано на $selectedTime", Toast.LENGTH_SHORT).show()

                    // Finish current activity
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this, "Пожалуйста, войдите в систему, чтобы забронировать номер", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Пожалуйста, введите корректные адреса", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateAddress(address: String): Boolean {
        return address.isNotEmpty() && address.length >= 5 // Adjust minimum length as needed
    }
}
