package com.example.taxipark.DatabaseHelper

import Order
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.taxipark.Driver
import com.example.taxipark.User


class DatabaseHelper(val context: Context,val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context,"DB",factory,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = "CREATE TABLE users (id INTEGER PRIMARY KEY, login TEXT, password TEXT)"
        val createDriversTable = "CREATE TABLE Drivers (\" +\n" +
                "                \"DriverID INTEGER PRIMARY KEY AUTOINCREMENT,\" +\n" +
                "                \"Name VARCHAR(100) NOT NULL,\" +\n" +
                "                \"LicenseNumber VARCHAR(50) NOT NULL,\" +\n" +
                "                \"PhoneNumber VARCHAR(15) NOT NULL,\" +\n" +
                "                \"Rating DECIMAL(3, 2) CHECK(Rating >= 0 AND Rating <= 5)\" +\n" +
                "                \")"
        db!!.execSQL(createUsersTable)
        db!!.execSQL(createDriversTable)
    }
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun onUpgrade1(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS Drivers")
        onCreate(db)
    }

    fun addUser(user: User){
        val values = ContentValues()
        values.put("login",user.login)
        values.put("password",user.password)

        val db = this.writableDatabase
        db.insert("users",null,values)
        db.close()

    }

    fun getUser(login:String,password:String):Boolean{
        val db = this.readableDatabase
        val result = db.rawQuery(" SELECT * FROM users WHERE login = '$login' AND password = '$password' ",null)
        return result.moveToFirst()
    }

    @SuppressLint("Range")
    fun getAllDrivers(): List<Driver> {
        val driverList: MutableList<Driver> = ArrayList<Driver>()
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM Drivers", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("DriverID"))
                val name = cursor.getString(cursor.getColumnIndex("Name"))
                val licenseNumber = cursor.getString(cursor.getColumnIndex("LicenseNumber"))
                val phoneNumber = cursor.getString(cursor.getColumnIndex("PhoneNumber"))
                val rating = cursor.getDouble(cursor.getColumnIndex("Rating"))

                val driver: Driver = Driver(id, name, licenseNumber, phoneNumber, rating)
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
        db.close()

        return ordersList
    }
}
