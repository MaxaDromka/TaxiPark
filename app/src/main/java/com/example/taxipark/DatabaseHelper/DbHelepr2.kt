package com.example.taxipark.DatabaseHelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.example.taxipark.User
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
        private const val DB_VERSION = 16
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

   /* @SuppressLint("Range")
    fun getUserBookings(userId: Int): List<String> {
        val bookingsList = mutableListOf<String>()
        val db = this.readableDatabase
        val query = """
        SELECT BookingID, PickupLocation, DropoffLocation, BookingDate, Status 
        FROM Bookings 
        INNER JOIN Orders ON Bookings.OrderID = Orders.OrderID 
        WHERE Orders.UserID = ?"""
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val pickupLocation = it.getString(it.getColumnIndex("PickupLocation"))
                    val dropoffLocation = it.getString(it.getColumnIndex("DropoffLocation"))
                    val bookingDate = it.getString(it.getColumnIndex("BookingDate"))
                    val status = it.getString(it.getColumnIndex("Status"))

                    bookingsList.add("Маршрут: $pickupLocation → $dropoffLocation\nДата: $bookingDate, Статус: $status")
                } while (it.moveToNext())
            }
        }

        return bookingsList
    }*/



    fun createBooking(userId: Int, orderId: Int, pickupLocation: String, dropoffLocation: String, status: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("PickupLocation", pickupLocation)
            put("DropoffLocation", dropoffLocation)
            put("Status", status)
            put("UserID", userId)
            put("BookingDate", System.currentTimeMillis())
        }

        try {
            val result = db.insertOrThrow("Reservation", null, values)
            if (result != -1L) {
                Log.d("NewBookingActivity", "Booking saved successfully!")
            } else {
                Log.d("NewBookingActivity", "Error saving booking")
            }
        } catch (e: SQLException) {
            Log.e("NewBookingActivity", "Error saving booking: ${e.message}")
            e.printStackTrace()
        }
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


    fun checkTableExists(tableName: String): Boolean {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
            "SELECT name FROM sqlite_master WHERE type='table' AND name=?",
            arrayOf(tableName)
        )
        val exists = cursor.count > 0
        cursor.close()
        return exists
    }

    // Метод для получения списка бронирований пользователя
    @SuppressLint("Range")
    fun getUserBookings(userId: Int): List<HashMap<String, Any>> {
        val bookings = ArrayList<HashMap<String, Any>>()

        val cursor = readableDatabase.rawQuery(
            """SELECT Booking.BookingID, Orders.OrderID, Booking.PickupLocation, Booking.DropoffLocation, Booking.Status, Booking.BookingDate
           FROM Booking
           LEFT JOIN Orders ON Booking.OrderID = Orders.OrderID
           WHERE Orders.UserID = ?""",
            arrayOf(userId.toString())
        )

        cursor.use {
            if (it.moveToFirst()) {
                do {
                    val booking = HashMap<String, Any>()
                    booking["BookingID"] = it.getInt(it.getColumnIndex("BookingID"))
                    booking["OrderID"] = it.getInt(it.getColumnIndex("OrderID"))
                    booking["PickupLocation"] = it.getString(it.getColumnIndex("PickupLocation"))
                    booking["DropoffLocation"] = it.getString(it.getColumnIndex("DropoffLocation"))
                    booking["Status"] = it.getString(it.getColumnIndex("Status"))
                    booking["BookingDate"] = it.getString(it.getColumnIndex("BookingDate"))
                    bookings.add(booking)
                } while (it.moveToNext())
            }
        }

        return bookings
    }

    @SuppressLint("Range")
    fun getUserById(userId: Int): User {
        val db = this.readableDatabase

        // SQL запрос для получения данных пользователя по его ID
        val query = "SELECT Username, Email, PhoneNumber FROM Users WHERE UserId = ?"

        // Используем курсор для извлечения данных
        val cursor = db.rawQuery(query, arrayOf(userId.toString()))

        // Проверка, если данные найдены
        if (cursor != null && cursor.moveToFirst()) {
            val username = cursor.getString(cursor.getColumnIndex("Username"))
            val email = cursor.getString(cursor.getColumnIndex("Email"))


            // Закрываем курсор после использования
            cursor.close()

            // Создаем и возвращаем объект пользователя
            return User(username, email)
        } else {
            cursor.close()
            // Если данные не найдены, выбрасываем исключение или возвращаем пустого пользователя
            throw Exception("User not found")
        }


    }



}