package com.example.taxipark

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
    private fun displayOrderInfo() {
        val orders = ArrayList<HashMap<String, Any>>()

        val cursor = mDb.rawQuery("SELECT * FROM Vehicles", null)
        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val driver = HashMap<String, Any>()
                    driver["DriverID"] = it.getString(1)
                    driver["Model"] = it.getString(2)
                    driver["LicwnswPlate"] = it.getString(3)
                    //driver["Status"] = it.getString(4)
                    // If Status is needed, uncomment and adjust accordingly
                    // driver["Status"] = it.getString(6)
                    orders.add(driver)
                } while (it.moveToNext())
            }
        }

        val from = arrayOf("DriverID", "Model", "LicwnswPlate")
        val to = intArrayOf(R.id.textViewAuto1, R.id.textViewAuto2, R.id.textViewAuto3)

        // Set up SimpleAdapter
        val adapter = SimpleAdapter(this, orders, R.layout.adapter_item_autos, from, to)

        // Set adapter to ListView
        listView.adapter = adapter

        // Optional header view setup if needed
        // val headerView = layoutInflater.inflate(R.layout.header_item, null)
        // listView.addHeaderView(headerView)
    }
}