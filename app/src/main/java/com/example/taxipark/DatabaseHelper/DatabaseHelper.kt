package com.example.taxipark.DatabaseHelper

import Order
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.taxipark.Driver
import com.example.taxipark.User

class DatabaseHelper(val context: Context, val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, "BDTAxiPark.db", null, 4) {


    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
        CREATE TABLE IF NOT EXISTS Photos (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            imagePath TEXT NOT NULL
        )
    """
        db.execSQL(createTableQuery)
    }

    fun addPhoto(imagePath: String) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put("imagePath", imagePath)
        }
        db.insert("Photos", null, values)
    }




    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        //db.execSQL("DROP TABLE IF EXISTS Drivers")
        onCreate(db)
    }

    fun addUser(user: User) {
        val values = ContentValues()
        values.put("login", user.login)
        values.put("password", user.password)

        val db = this.writableDatabase
        db.insert("users", null, values)
        db.close()
    }

    fun getUser(login: String, password: String): Boolean {
        val db = this.readableDatabase
        val result = db.rawQuery("SELECT * FROM users WHERE login = ? AND password = ?", arrayOf(login, password))
        return result.moveToFirst().also { result.close() }
    }

    @SuppressLint("Range")
    fun getAllDrivers(): List<Driver> {
        val driverList: MutableList<Driver> = ArrayList()
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM Drivers", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("DriverID"))
                val name = cursor.getString(cursor.getColumnIndex("Name"))
                val licenseNumber = cursor.getString(cursor.getColumnIndex("LicenseNumber"))
                val phoneNumber = cursor.getString(cursor.getColumnIndex("PhoneNumber"))
                val rating = cursor.getDouble(cursor.getColumnIndex("Rating"))

                val driver = Driver(id, name, licenseNumber, phoneNumber, rating)
                driverList.add(driver)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return driverList
    }

    fun addOrder(order: Order) {
        val values = ContentValues()
        values.put("orderID", order.orderID)
        values.put("driverID", order.driverID)
        values.put("userID", order.userID)
        values.put("pickupLocation", order.pickupLocation)
        values.put("dropoffLocation", order.dropoffLocation)
        values.put("status", order.status)

        val db = this.writableDatabase
        db.insert("orders", null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllOrders(): List<Order> {
        val ordersList = mutableListOf<Order>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM orders", null)

        if (cursor.moveToFirst()) {
            do {
                val orderID = cursor.getInt(cursor.getColumnIndex("orderID"))
                val driverID = cursor.getInt(cursor.getColumnIndex("driverID"))
                val userID = cursor.getInt(cursor.getColumnIndex("userID"))
                val pickupLocation = cursor.getString(cursor.getColumnIndex("pickupLocation"))
                val dropoffLocation = cursor.getString(cursor.getColumnIndex("dropoffLocation"))
                val status = cursor.getString(cursor.getColumnIndex("status"))

                val order = Order(orderID, driverID, userID, pickupLocation, dropoffLocation, status)
                ordersList.add(order)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return ordersList
    }
}