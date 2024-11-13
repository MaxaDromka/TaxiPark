package com.example.taxipark

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.taxipark.DatabaseHelper.DatabaseHelper

class RegistrationActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registration)

        val userLogin:EditText = findViewById(R.id.user_login)
        val userPassword:EditText = findViewById(R.id.user_password)
        val button:Button = findViewById(R.id.button)
        val link:TextView = findViewById(R.id.link_to_auth)

        link.setOnClickListener{
            val intent = Intent(this,AuthActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener{
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if(login == "" || password == "")
                Toast.makeText(this,"Не все поля заполнены",Toast.LENGTH_LONG).show()
            else{
                val user = User(login,password)

                val db = DatabaseHelper(this,null)
                db.addUser(user)
                Toast.makeText(this,"Пользователь $login добавлен",Toast.LENGTH_LONG).show()

                userLogin.text.clear()
                userPassword.text.clear()
            }
        }
    }
}
