package com.example.taxipark.DatabaseHelper

import Order
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.taxipark.User


class DatabaseHelper(val context: Context,val factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context,"DB",factory,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = "CREATE TABLE users (id INTEGER PRIMARY KEY, login TEXT, password TEXT)"
        db!!.execSQL(createUsersTable)

    }
    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS users")
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
