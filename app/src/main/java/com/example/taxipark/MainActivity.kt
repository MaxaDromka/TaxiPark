package com.example.taxipark

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth;
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.example.taxipark.DatabaseHelper.DatabaseHelper


class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        databaseHelper = DatabaseHelper(this,null)

        val buttonAuto = findViewById<Button>(R.id.carsButton)

        buttonAuto.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(intent)
        }



        val buttoDrivers = findViewById<Button>(R.id.driversButton)
        buttoDrivers.setOnClickListener{
            val intent = Intent(this@MainActivity, DriverActivity::class.java)
            startActivity(intent)
        }


        val buttoOrders = findViewById<Button>(R.id.ordersButton)
        buttoOrders.setOnClickListener{
            val intent = Intent(this@MainActivity, OrdersActivity::class.java)
            startActivity(intent)
        }

        val buttonRegistration = findViewById<Button>(R.id.loginButton)
        buttonRegistration.setOnClickListener{
            val intent = Intent(this@MainActivity,RegistrationActivity::class.java)
            startActivity(intent)
        }

        val buttonAuth = findViewById<Button>(R.id.authButton)
        buttonAuth.setOnClickListener{
            val intent = Intent(this@MainActivity,AuthActivity::class.java)
            startActivity(intent)
        }
        val bookingsButton = findViewById<Button>(R.id.bookingsButton)
        bookingsButton.setOnClickListener {
            val intent = Intent(this@MainActivity, BookingsActivity::class.java)
            startActivity(intent)
        }

        val profileButton = findViewById<Button>(R.id.profileButton)
        profileButton.setOnClickListener {
            val intent = Intent(this@MainActivity, ProfileActivity::class.java)
            startActivity(intent)
        }



    }
}