package com.example.coinfin.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coinfin.utils.FirestoreManager
import com.example.coinfin.databinding.ActivityLoginBinding
import com.example.coinfin.utils.AuthManager
import com.google.android.ads.mediationtestsuite.activities.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private var isLoginMode = true


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toggleModeButton.setOnClickListener {
            isLoginMode = !isLoginMode
            updateUI()
        }

        binding.submitButton.setOnClickListener {
            val email = binding.usernameEdit.text.toString()
            val password = binding.passwordEdit.text.toString()
            val username = binding.usernameEdit.text.toString()

            if (email.isBlank() || password.isBlank() || (!isLoginMode && username.isBlank())) {
                Toast.makeText(this, "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isLoginMode) {
                AuthManager.signIn(email, password) { success, error ->
                    if (success) startHome()
                    else Toast.makeText(this, "Login failed: $error", Toast.LENGTH_SHORT).show()
                }
            } else {
                AuthManager.signUp(email, password) { success, user, error ->
                    if (success && user != null) {
                        FirestoreManager.saveUserProfile(user.uid, email, username) { saved ->
                            if (saved) startHome()
                            else Toast.makeText(this, "Error saving user", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Signup failed: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        updateUI()
    }

    private fun updateUI() {
        binding.usernameEdit.visibility = if (isLoginMode) View.GONE else View.VISIBLE
        binding.toggleModeButton.text = if (isLoginMode) "No account? Sign up" else "Have an account? Log in"
        binding.submitButton.text = if (isLoginMode) "Login" else "Sign Up"
    }

    private fun startHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
