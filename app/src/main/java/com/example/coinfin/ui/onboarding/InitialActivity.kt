package com.example.coinfin.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coinfin.databinding.ActivityInitialBinding
import com.example.coinfin.utils.AuthManager
import com.example.coinfin.utils.FirestoreManager
import com.example.coinfin.MainActivity
import com.google.firebase.firestore.FirebaseFirestore

class InitialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInitialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInitialBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val username = binding.usernameEditText.text.toString()

            if (email.isBlank() || password.isBlank() || username.isBlank()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AuthManager.signUp(email, password) { success, user, error ->
                if (success && user != null) {
                    val uid = user.uid

                    FirebaseFirestore.getInstance().collection("users").document(uid)
                        .set(mapOf("email" to email, "username" to username))
                        .addOnSuccessListener {
                            Toast.makeText(this, "Usuario creado", Toast.LENGTH_SHORT).show()

                            // ✅ Redirigir a MainActivity
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Error al guardar datos", Toast.LENGTH_SHORT).show()
                        }

                } else {
                    Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
