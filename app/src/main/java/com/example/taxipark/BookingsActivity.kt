package com.example.taxipark

import android.annotation.SuppressLint
import android.content.Intent
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taxipark.DatabaseHelper.DbHelepr2
import java.io.IOException

class BookingsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DbHelepr2
    private lateinit var mDb: SQLiteDatabase
    private lateinit var bookingsRecyclerView: RecyclerView
    private lateinit var newBookingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bookings_activity)

        bookingsRecyclerView = findViewById(R.id.bookingsRecyclerView)
        newBookingButton = findViewById(R.id.newBookingButton)

        // Загружаем бронирования для текущего пользователя
        //loadBookings()

        // Кнопка для добавления нового бронирования
        newBookingButton.setOnClickListener {
            val intent = Intent(this, NewBookingActivity::class.java)
            startActivity(intent)
        }

        dbHelper = DbHelepr2(this)

        try {
            dbHelper.updateDataBase()
            mDb = dbHelper.writableDatabase
            loadBookings() // Call method to display orders
        } catch (e: IOException) {
            throw Error("UnableToUpdateDatabase")
        } catch (e: SQLException) {
            throw e
        }



    }

    @SuppressLint("Range")
    private fun loadBookings() {
        val sharedPreferences = getSharedPreferences("TaxiParkPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("LoggedInUserId", -1)

        if (userId != -1) {
            val bookings = ArrayList<HashMap<String, Any>>()
            val cursor = mDb.rawQuery(
                """SELECT Reservation.ReservationID, Reservation.PickupLocation,
              Reservation.DropoffLocation, Reservation.Status, Reservation.BookingDate
       FROM Reservation
       WHERE Reservation.UserID = ?""",
                arrayOf(userId.toString())
            )


            cursor.use {
                if (it.moveToFirst()) {
                    do {
                        val booking = HashMap<String, Any>()
                        booking["BookingID"] = it.getInt(it.getColumnIndex("ReservationID"))
                        booking["PickupLocation"] = it.getString(it.getColumnIndex("PickupLocation"))
                        booking["DropoffLocation"] = it.getString(it.getColumnIndex("DropoffLocation"))
                        booking["Status"] = it.getString(it.getColumnIndex("Status"))
                        booking["BookingDate"] = it.getString(it.getColumnIndex("BookingDate"))
                        bookings.add(booking)
                    } while (it.moveToNext())
                }
            }

            if (bookings.isNotEmpty()) {
                val adapter = BookingsAdapter(this, bookings)
                bookingsRecyclerView.layoutManager = LinearLayoutManager(this)
                bookingsRecyclerView.adapter = adapter
            } else {
                Toast.makeText(this, "Нет бронирований", Toast.LENGTH_SHORT).show()
            }
        }
    }









}

