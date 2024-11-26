package com.example.taxipark

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taxipark.DatabaseHelper.DatabaseHelper

class DriverActivity : AppCompatActivity() {

    private lateinit  var databaseHelper: DatabaseHelper
    private lateinit var driverInfoTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drivers)

        driverInfoTextView = findViewById(R.id.driver_info_text_view)

        // Initialize DatabaseHelper
        databaseHelper = DatabaseHelper(this,null)

        // Fetch and display driver information
        displayDriverInfo()
    }

    @SuppressLint("Range")
    private fun displayDriverInfo() {
        val db = databaseHelper.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Drivers", null)

        if (cursor.moveToFirst()) {
            val driverId = cursor.getInt(cursor.getColumnIndex("DriverID"))
            val name = cursor.getString(cursor.getColumnIndex("Name"))
            val licenseNumber = cursor.getString(cursor.getColumnIndex("LicenseNumber"))
            val phoneNumber = cursor.getString(cursor.getColumnIndex("PhoneNumber"))
            val rating = cursor.getDouble(cursor.getColumnIndex("Rating"))

            // Format the driver information
            val driverInfo = "Driver ID: $driverId\n" +
                    "Name: $name\n" +
                    "License Number: $licenseNumber\n" +
                    "Phone Number: $phoneNumber\n" +
                    "Rating: $rating"

            // Display the information in TextView
            driverInfoTextView.text = driverInfo
        } else {
            driverInfoTextView.text = "No driver information available."
        }

        cursor.close()
        db.close()
    }
}