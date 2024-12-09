package com.example.taxipark.DatabaseHelper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.sql.SQLException

class DbHelepr2 (context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    private val mContext: Context = context
    private var mDataBase: SQLiteDatabase? = null
    private var mNeedUpdate = false

    private val DB_PATH: String = if (Build.VERSION.SDK_INT >= 17) {
        "${mContext.applicationInfo.dataDir}/databases/"
    } else {
        "/data/data/${mContext.packageName}/databases/"
    }

    companion object {
        private const val DB_NAME = "BDTAxiPark.db"
        private const val DB_VERSION = 8
    }

    init {
        copyDataBase()
        readableDatabase // Вызывает создание базы данных
    }

    @Throws(IOException::class)
    fun updateDataBase() {
        if (mNeedUpdate) {
            val dbFile = File(DB_PATH + DB_NAME)
            if (dbFile.exists()) dbFile.delete()
            copyDataBase()
            mNeedUpdate = false
        }
    }

    private fun checkDataBase(): Boolean {
        val dbFile = File(DB_PATH + DB_NAME)
        return dbFile.exists()
    }

    private fun copyDataBase() {
        if (!checkDataBase()) {
            readableDatabase // Создает базу данных, если она не существует
            close()
            try {
                copyDBFile()
            } catch (mIOException: IOException) {
                throw Error("ErrorCopyingDataBase")
            }
        }
    }

    @Throws(IOException::class)
    private fun copyDBFile() {
        val mInput: InputStream = mContext.assets.open(DB_NAME)
        val mOutput: OutputStream = FileOutputStream(DB_PATH + DB_NAME)
        val mBuffer = ByteArray(1024)
        var mLength: Int
        while (mInput.read(mBuffer).also { mLength = it } > 0) {
            mOutput.write(mBuffer, 0, mLength)
        }
        mOutput.flush()
        mOutput.close()
        mInput.close()
    }

    @Throws(SQLException::class)
    fun openDataBase(): Boolean {
        mDataBase = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null, SQLiteDatabase.CREATE_IF_NECESSARY)
        return mDataBase != null
    }

    override fun close() {
        mDataBase?.close()
        super.close()
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Здесь можно создать таблицы, если это необходимо
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (newVersion > oldVersion) {
            mNeedUpdate = true
        }
    }

    fun getUserByUsernameAndPassword(username: String, password: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM Users WHERE Username = ? AND Password = ?", arrayOf(username, password))

        return cursor.count > 0
    }

    fun getUserBookings(userId: Int): List<String> {
        val bookings = mutableListOf<String>()
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT b.BookingID, b.BookingDate, b.PickupLocation, b.DropoffLocation, b.Status " +
                    "FROM Bookings b " +
                    "JOIN Orders o ON b.OrderID = o.OrderID " +
                    "JOIN Users u ON o.UserID = u.UserID " +
                    "WHERE u.UserID = ?",
            arrayOf(userId.toString())
        )

        while (cursor.moveToNext()) {
            val bookingDetails = "${cursor.getString(1)}: ${cursor.getString(2)} -> ${cursor.getString(3)} | ${cursor.getString(4)} | User: ${cursor.getString(5)}"
            bookings.add(bookingDetails)
        }
        cursor.close()
        return bookings
    }


    fun createBooking(orderId: Int, pickupLocation: String, dropoffLocation: String, status: String, userId: Int): Long {
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put("OrderID", orderId)
            put("BookingDate", System.currentTimeMillis())
            put("PickupLocation", pickupLocation)
            put("DropoffLocation", dropoffLocation)
            put("Status", status)
        }
        return db.insert("Bookings", null, contentValues)
    }

    fun getUserIdByUsername(username: String): Int {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT UserID FROM Users WHERE Username = ?", arrayOf(username))
        var userId = -1
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0)
        }
        cursor.close()
        return userId
    }




}