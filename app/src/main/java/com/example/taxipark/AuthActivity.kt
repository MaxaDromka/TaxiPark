package com.example.taxipark

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

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)
        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPassword: EditText = findViewById(R.id.user_password_auth)
        val button: Button = findViewById(R.id.button)
        val linkToReg:TextView = findViewById(R.id.link_to_reg)

        linkToReg.setOnClickListener{
            val intent = Intent(this,RegistrationActivity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener{
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()

            if(login == "" || password == "")
                Toast.makeText(this,"Не все поля заполнены", Toast.LENGTH_LONG).show()
            else{

                val db = DatabaseHelper(this,null)
                val isAuth = db.getUser(login,password)

                if(isAuth){
                Toast.makeText(this,"Пользователь $login авторизован", Toast.LENGTH_LONG).show()
                userLogin.text.clear()
                userPassword.text.clear()
                }
                else{
                    Toast.makeText(this,"Пользователь $login не авторизован", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
