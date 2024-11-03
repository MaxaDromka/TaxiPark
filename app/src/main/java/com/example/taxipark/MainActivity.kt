package com.example.taxipark

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)




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



    }
}