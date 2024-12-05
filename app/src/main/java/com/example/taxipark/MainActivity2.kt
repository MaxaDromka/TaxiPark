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
                    driver["Model"] = it.getString(2)
                    driver["LicwnswPlate"] = it.getString(3)
                    orders.add(driver)
                } while (it.moveToNext())
            }
        }

        // Use the custom adapter
        val adapter = VehicleAdapter(this, orders)
        listView.adapter = adapter
    }

}