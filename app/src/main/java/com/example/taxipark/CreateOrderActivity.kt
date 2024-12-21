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
    private val driverIdMap = HashMap<String, Int>()

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

            // Получаем выбранное имя водителя из Spinner
            val selectedDriverName = driverSpinner.selectedItem as String

            if (pickupLocation.isNotEmpty() && dropoffLocation.isNotEmpty()) {
                // Получаем ID выбранного водителя из мапы
                val selectedDriverId = driverIdMap[selectedDriverName] ?: -1

                if (selectedDriverId != -1) {
                    saveOrder(selectedDriverId, pickupLocation, dropoffLocation)

                    // Переход к OrdersActivity и завершение текущей активности
                    val intent = Intent(this, OrdersActivity::class.java)
                    startActivity(intent)
                    finish() // Закрываем текущую активность
                } else {
                    Toast.makeText(this, "Ошибка при выборе водителя", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("Range")
    private fun loadDrivers() {
        val driverNames = ArrayList<String>() // Список для имен водителей
        val cursor = mDb.rawQuery("SELECT DriverID, Name FROM Drivers", null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val driverID = it.getInt(it.getColumnIndex("DriverID"))
                    val name = it.getString(it.getColumnIndex("Name"))

                    driverNames.add(name) 
                    driverIdMap[name] = driverID // Сохраняем соответствие имя -> ID
                } while (it.moveToNext())
            }
        }

        // Установите адаптер для Spinner с именами водителей
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, driverNames)
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