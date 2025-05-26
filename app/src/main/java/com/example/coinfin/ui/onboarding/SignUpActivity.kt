package com.example.coinfin.ui.onboarding

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coinfin.databinding.ActivitySignUpBinding
import com.example.coinfin.utils.AuthManager
import com.example.coinfin.utils.FirestoreManager

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            val username = binding.usernameEditText.text.toString()

            if (email.isNotBlank() && password.isNotBlank() && username.isNotBlank()) {
                AuthManager.signUp(email, password) { success, user, error ->
                    if (success && user != null) {
                        FirestoreManager.saveUserProfile(user.uid, email, username) { saved ->
                            if (saved) {
                                Toast.makeText(this, "Registrado correctamente", Toast.LENGTH_SHORT).show()
                                finish()
                            } else {
                                Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
