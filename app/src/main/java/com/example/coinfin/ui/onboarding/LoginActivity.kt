package com.example.coinfin.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coinfin.R
import com.example.coinfin.ui.home.MainActivity
import com.example.coinfin.utils.AuthManager

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AuthManager.signIn(email, password) { success, error ->
                progressBar.visibility = View.VISIBLE
                if (success) {
                    progressBar.visibility = View.GONE
                    // Login exitoso, redirige a MainActivity
                    startActivity(Intent(this, com.example.coinfin.ui.home.MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val linkToRegister: TextView = findViewById(R.id.linkToRegister)

        linkToRegister.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }
}
