package com.example.taxipark

import Order
import android.annotation.SuppressLint
import android.content.Intent
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import android.widget.SimpleAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taxipark.DatabaseHelper.DbHelepr2
import java.io.IOException
class OrdersActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DbHelepr2
    private lateinit var mDb: SQLiteDatabase
    private lateinit var listView: ListView // Declare ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_order) // Ensure this is the correct layout for displaying orders

        listView = findViewById(R.id.listView) // Initialize ListView


        val createOrderButton = findViewById<Button>(R.id.createOrder)
        createOrderButton.setOnClickListener {
            val intent = Intent(this, CreateOrderActivity::class.java)
            startActivity(intent)
        }

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

        // Execute the query
        val cursor = mDb.rawQuery("SELECT * FROM Orders", null)

        // Check if cursor is not null and has data
        cursor.use {
            if (it != null && it.moveToFirst()) {
                do {
                    val order = HashMap<String, Any>()
                    order["OrderID"] = it.getString(it.getColumnIndex("OrderID")) ?: "N/A"
                    order["DriverID"] = it.getString(it.getColumnIndex("DriverID")) ?: "N/A"
                    order["VehicleID"] = it.getString(it.getColumnIndex("VehicleID")) ?: "N/A"
                    order["PickupLocation"] = it.getString(it.getColumnIndex("PickupLocation")) ?: "N/A"
                    order["DropoffLocation"] = it.getString(it.getColumnIndex("DropoffLocation")) ?: "N/A"
                    // If Status is needed, uncomment and adjust accordingly
                    // order["Status"] = it.getString(it.getColumnIndex("Status")) ?: "N/A"
                    orders.add(order)
                } while (it.moveToNext())
            }
        }

        val from = arrayOf("OrderID", "DriverID", "VehicleID", "PickupLocation", "DropoffLocation")
        val to = intArrayOf(R.id.textView, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5)

        // Set up SimpleAdapter
        val adapter = SimpleAdapter(this, orders, R.layout.adapteritemorders, from, to)

        // Set adapter to ListView
        listView.adapter = adapter

        // Optional header view setup if needed
        // val headerView = layoutInflater.inflate(R.layout.header_item, null)
        // listView.addHeaderView(headerView)
    }
}