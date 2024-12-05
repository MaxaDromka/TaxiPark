package com.example.taxipark

import android.annotation.SuppressLint
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.SimpleAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taxipark.DatabaseHelper.DbHelepr2
import java.io.IOException

class DriverActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DbHelepr2
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
            mDb = databaseHelper.writableDatabase
            displayDriverInfo()
        } catch (mIOException: IOException) {
            throw Error("UnableToUpdateDatabase")
        } catch (mSQLException: SQLException) {
            throw mSQLException
        }
    }

    @SuppressLint("Range")
    private fun displayDriverInfo() {
        val drivers = ArrayList<HashMap<String, Any>>()

        val cursor = mDb.rawQuery("SELECT * FROM Drivers", null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val driver = HashMap<String, Any>()

                    // Fill the driver details
                    driver["Name"] = it.getString(1)
                    driver["LicenseNumber"] = it.getString(2)
                    driver["PhoneNumber"] = it.getString(3)
                    driver["Rating"] = it.getString(4)

                    drivers.add(driver)
                } while (it.moveToNext())
            }
        }

        // Create a custom adapter instead of SimpleAdapter for better control over layout
        val adapter = object : BaseAdapter() {
            override fun getCount(): Int = drivers.size

            override fun getItem(position: Int): Any = drivers[position]

            override fun getItemId(position: Int): Long = position.toLong()

            override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
                val view = convertView ?: layoutInflater.inflate(R.layout.adapter_item, parent, false)

                // Get references to the TextViews in the layout
                val nameTextView = view.findViewById<TextView>(R.id.textView)
                val licenseNumberTextView = view.findViewById<TextView>(R.id.textView2)
                val phoneNumberTextView = view.findViewById<TextView>(R.id.textView3)
                val ratingTextView = view.findViewById<TextView>(R.id.textView4)

                // Populate the TextViews with labeled data
                val driverData = drivers[position]
                nameTextView.text = "Имя водителя: ${driverData["Name"]}"
                licenseNumberTextView.text = "Номер лицензии: ${driverData["LicenseNumber"]}"
                phoneNumberTextView.text = "Телефонный номер: ${driverData["PhoneNumber"]}"
                ratingTextView.text = "Рейтинг: ${driverData["Rating"]}"

                return view
            }
        }

        // Set adapter to ListView
        driverInfoTextView.adapter = adapter
    }
}