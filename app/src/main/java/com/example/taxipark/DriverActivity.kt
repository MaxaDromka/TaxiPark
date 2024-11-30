package com.example.taxipark

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taxipark.DatabaseHelper.DbHelepr2
import java.io.IOException

class DriverActivity : AppCompatActivity() {

    private lateinit  var databaseHelper: DbHelepr2
    private lateinit var driverInfoTextView: ListView
    private lateinit var mDb: SQLiteDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drivers)

        driverInfoTextView = findViewById(R.id.listView)

        // Initialize DatabaseHelper
        databaseHelper = DbHelepr2(this)
        try {
            databaseHelper.updateDataBase()
        } catch (mIOException: IOException) {
            throw Error("UnableToUpdateDatabase")
        }

        try {
            mDb = databaseHelper.writableDatabase
        } catch (mSQLException: SQLException) {
            throw mSQLException
        }

        // Fetch and display driver information
        displayDriverInfo()
    }

    @SuppressLint("Range")
    private fun displayDriverInfo() {
        //val db = databaseHelper.readableDatabase
        val drivers = ArrayList<HashMap<String, Any>>()
        val cursor = mDb.rawQuery("SELECT * FROM Drivers", null)
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    // Список параметров конкретного клиента
                    val driver = HashMap<String, Any>()

                    // Заполняем клиента
                    driver["Name"] = it.getString(1)
                    driver["LicenseNumber"] = it.getString(2)
                    driver["PhoneNumber"] = it.getString(3)
                    driver["Rating"] = it.getString(4)

                    // Закидываем клиента в список клиентов
                    drivers.add(driver)
                } while (it.moveToNext())
            }
        }
        // Какие параметры клиента мы будем отображать в соответствующих элементах из разметки adapter_item.xml
        val from = arrayOf("Name", "LicenseNumber","PhoneNumber","Rating")
        val to = intArrayOf(R.id.textView,R.id.textView2,R.id.textView3,R.id.textView4)

        // Создаем адаптер
        val adapter = SimpleAdapter(this, drivers, R.layout.adapter_item, from, to)
        val headerView = layoutInflater.inflate(R.layout.header_item, null)
        driverInfoTextView.addHeaderView(headerView)
        val listView: ListView = findViewById(R.id.listView)
        listView.adapter = adapter
    }
}