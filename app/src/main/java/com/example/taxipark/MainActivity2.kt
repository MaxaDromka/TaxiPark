package com.example.taxipark

import android.annotation.SuppressLint
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taxipark.DatabaseHelper.DatabaseHelper;
import com.example.taxipark.DatabaseHelper.DbHelepr2
import java.io.IOException

class MainActivity2 : AppCompatActivity() {


    private lateinit var databaseHelper: DbHelepr2
    private lateinit var mDb: SQLiteDatabase

    private lateinit var listView: ListView // Declare ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
            setContentView(R.layout.item_autos)

                listView = findViewById(R.id.listViewAuto) // Initialize ListView

        databaseHelper = DbHelepr2(this)
        try {
            databaseHelper.updateDataBase()
            mDb = databaseHelper.writableDatabase
            displayOrderInfo() // Call method to display orders
        } catch (e: IOException) {
            throw Error("UnableToUpdateDatabase")
        } catch (e: SQLException) {
            throw e
        }

    }
    @SuppressLint("Range")
    private fun displayOrderInfo() {
        val orders = ArrayList<HashMap<String, Any>>()

        // Adjust the query if needed to include specific columns
        val cursor = mDb.rawQuery("SELECT * FROM Vehicles", null)
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val vehicle = HashMap<String, Any>()
                    vehicle["Model"] = it.getString(it.getColumnIndex("Model")) ?: "N/A"
                    vehicle["LicensePlate"] = it.getString(it.getColumnIndex("LicensePlate")) ?: "N/A"
                    vehicle["StatusAuto"] = it.getString(it.getColumnIndex("StatusAuto")) ?: "N/A" // Ensure this is correct
                    orders.add(vehicle)
                } while (it.moveToNext())
            }
        }

        // Use the custom adapter
        val adapter = VehicleAdapter(this, orders)
        listView.adapter = adapter
    }

}