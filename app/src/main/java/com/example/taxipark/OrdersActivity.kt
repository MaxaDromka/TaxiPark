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
    private lateinit var recyclerView: RecyclerView // Change ListView to RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.item_order) // Ensure this is the correct layout for displaying orders

        recyclerView = findViewById(R.id.recycler_view) // Initialize RecyclerView

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

        // Execute the query with a JOIN to get Driver Name
        val cursor = mDb.rawQuery("""SELECT Orders.OrderID, Drivers.Name AS DriverName, Orders.VehicleID,Orders.PickupLocation, Orders.DropoffLocation, Orders.Status FROM Orders JOIN Drivers ON Orders.DriverID = Drivers.DriverID """, null)

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val order = HashMap<String, Any>()
                    order["OrderID"] = it.getString(it.getColumnIndex("OrderID")) ?: "N/A"
                    order["DriverName"] = it.getString(it.getColumnIndex("DriverName")) ?: "N/A" // Get Driver Name
                    order["VehicleID"] = it.getString(it.getColumnIndex("VehicleID")) ?: "N/A"
                    order["PickupLocation"] = it.getString(it.getColumnIndex("PickupLocation")) ?: "N/A"
                    order["DropoffLocation"] = it.getString(it.getColumnIndex("DropoffLocation")) ?: "N/A"
                    order["Status"] = it.getString(it.getColumnIndex("Status")) ?: "N/A"
                    orders.add(order)
                } while (it.moveToNext())
            }
        }

        // Set up RecyclerAdapter instead of SimpleAdapter
        val adapter = OrdersAdapter(this, orders)

        // Set up Recycler View
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}