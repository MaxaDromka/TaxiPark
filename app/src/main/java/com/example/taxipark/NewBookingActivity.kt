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
                // Получаем ID текущего пользователя из SharedPreferences
                val sharedPreferences = getSharedPreferences("TaxiParkPrefs", MODE_PRIVATE)
                val userId = sharedPreferences.getInt("LoggedInUserId", -1)

                if (userId != -1) {
                    // Создаем бронирование с учетом UserID
                    val orderId = 1  // В реальном приложении OrderID должен быть динамическим
                    val status = "Confirmed"
                    dbHelper.createBooking(userId, orderId, pickup, dropoff, status)

                    // Уведомление об успешном бронировании
                    Toast.makeText(this, "Booking Confirmed", Toast.LENGTH_SHORT).show()

                    // Завершаем текущую активность
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this, "Please log in to make a booking", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter both locations", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


