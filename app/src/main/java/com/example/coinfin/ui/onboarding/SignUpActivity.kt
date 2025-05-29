package com.example.coinfin.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coinfin.R
import com.example.coinfin.ui.home.MainActivity
import com.example.coinfin.utils.AuthManager
import com.example.coinfin.utils.FirestoreManager

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val emailEditText: EditText = findViewById(R.id.emailEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val signUpButton: Button = findViewById(R.id.signUpButton)
        val linkToLogin: TextView = findViewById(R.id.linkToLogin)

        fun isValidPassword(password: String): Boolean {
            val passwordPattern = "^(?=.*[A-Z])(?=.*\\d).{8,}\$"
            return password.matches(passwordPattern.toRegex())
        }

        linkToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val username = usernameEditText.text.toString()

            if (email.isBlank() || password.isBlank() || username.isBlank()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AuthManager.signUp(email, password) { success, user, error ->
                if (success && user != null && isValidPassword(password)) {
                    FirestoreManager.saveUserProfile(user.uid, email, username) { saved ->
                        if (saved) {
                            Toast.makeText(this, "Usuario registrado", Toast.LENGTH_SHORT).show()
                            supportActionBar?.setDisplayHomeAsUpEnabled(true)
                            startActivity(Intent(this, com.example.coinfin.ui.home.MainActivity::class.java))
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                            finish()
                        } else {
                            Toast.makeText(this, "Error al guardar datos", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    if (!isValidPassword(password)) Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres, una mayúscula y un número.", Toast.LENGTH_SHORT).show()
                    Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
