package com.example.taxipark

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taxipark.DatabaseHelper.DbHelepr2

class CreateOrderActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DbHelepr2
    private lateinit var mDb: SQLiteDatabase
    private lateinit var driverSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_order)

        databaseHelper = DbHelepr2(this)
        mDb = databaseHelper.writableDatabase

        driverSpinner = findViewById(R.id.driverSpinner)
        val pickupLocationEditText = findViewById<EditText>(R.id.pickupLocation)
        val dropoffLocationEditText = findViewById<EditText>(R.id.dropoffLocation)

        // Load drivers into spinner
        loadDrivers()

        val saveOrderButton = findViewById<Button>(R.id.saveOrderButton)

        saveOrderButton.setOnClickListener {
            val pickupLocation = pickupLocationEditText.text.toString()
            val dropoffLocation = dropoffLocationEditText.text.toString()
            val selectedDriver = driverSpinner.selectedItem as Driver // Cast selected item to Driver

            if (pickupLocation.isNotEmpty() && dropoffLocation.isNotEmpty()) {
                saveOrder(selectedDriver.driverID, pickupLocation, dropoffLocation)

                // Start OrdersActivity and finish this activity
                val intent = Intent(this, OrdersActivity::class.java)
                startActivity(intent)
                finish() // Close this activity
            } else {
                // Optionally show an error message
            }
        }
    }

    @SuppressLint("Range")
    private fun loadDrivers() {
        val drivers = ArrayList<Driver>() // Use Driver objects instead of HashMap
        val cursor = mDb.rawQuery("SELECT DriverID, Name, LicenseNumber, PhoneNumber, Rating FROM Drivers", null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val driverID = it.getInt(it.getColumnIndex("DriverID"))
                    val name = it.getString(it.getColumnIndex("Name"))
                    val licenseNumber = it.getString(it.getColumnIndex("LicenseNumber"))
                    val phoneNumber = it.getString(it.getColumnIndex("PhoneNumber"))
                    val rating = it.getDouble(it.getColumnIndex("Rating"))

                    // Create Driver object and add to list
                    drivers.add(Driver(driverID, name, licenseNumber, phoneNumber, rating))
                } while (it.moveToNext())
            }
        }

        // Set up ArrayAdapter for Spinner using Driver objects
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, drivers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        driverSpinner.adapter = adapter
    }

    private fun saveOrder(driverID: Int, pickupLocation: String, dropoffLocation: String) {
        // Получение текущего UserID из SharedPreferences
        val sharedPreferences = getSharedPreferences("TaxiParkPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getInt("LoggedInUserId", -1)

        if (userId != -1) {
            // Сохранение заказа в базу данных
            val values = ContentValues().apply {
                put("PickupLocation", pickupLocation)
                put("DropoffLocation", dropoffLocation)
                put("DriverID", driverID) // ID выбранного водителя
                put("UserID", userId) // ID авторизованного пользователя
                put("Status", "В ожидании") // Статус заказа
            }

            mDb.insert("Orders", null, values)

            // Уведомление пользователя об успешном создании заказа
            Toast.makeText(this, "Order Created Successfully", Toast.LENGTH_SHORT).show()
        } else {
            // Если пользователь не авторизован
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

}