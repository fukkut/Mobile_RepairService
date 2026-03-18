package com.example.repairservice.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.repairservice.MainActivity
import com.example.repairservice.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val editLogin = findViewById<android.widget.EditText>(R.id.editLogin)
        val editPassword = findViewById<android.widget.EditText>(R.id.editPassword)
        val btnLogin = findViewById<android.widget.TextView>(R.id.btnLogin)
        val errorBlock = findViewById<android.widget.LinearLayout>(R.id.errorBlock)
        val textError = findViewById<android.widget.TextView>(R.id.textError)

        // Контейнери полів вводу (LinearLayout навколо EditText)
        val loginField = editLogin.parent as View
        val passwordField = editPassword.parent as View

        btnLogin.setOnClickListener {
            val login = editLogin.text.toString().trim()
            val password = editPassword.text.toString().trim()

            // Скидаємо стан помилки
            errorBlock.visibility = View.GONE
            loginField.setBackgroundResource(R.drawable.bg_input)
            passwordField.setBackgroundResource(R.drawable.bg_input)

            // Перевірка на порожні поля
            if (login.isEmpty() || password.isEmpty()) {
                textError.text = "Заповніть всі поля"
                errorBlock.visibility = View.VISIBLE

                if (login.isEmpty()) {
                    loginField.setBackgroundResource(R.drawable.bg_input_error)
                }
                if (password.isEmpty()) {
                    passwordField.setBackgroundResource(R.drawable.bg_input_error)
                }
                return@setOnClickListener
            }

            // Перевірка логіну та паролю
            if (login == "admin" && password == "admin") {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                textError.text = "Невірний логін або пароль"
                errorBlock.visibility = View.VISIBLE
                loginField.setBackgroundResource(R.drawable.bg_input_error)
                passwordField.setBackgroundResource(R.drawable.bg_input_error)
            }
        }
    }
}