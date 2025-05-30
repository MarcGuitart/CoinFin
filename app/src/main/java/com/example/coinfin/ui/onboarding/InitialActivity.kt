package com.example.coinfin.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.coinfin.R
import com.google.firebase.auth.FirebaseAuth
import com.example.coinfin.ui.onboarding.SignUpActivity
import com.example.coinfin.ui.onboarding.LoginActivity

class InitialActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        android.util.Log.d("CoinFin", "InitialActivity started correctamente")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        val loginButton: Button = findViewById(R.id.loginButton)
        val signUpButton: Button = findViewById(R.id.signUpButton)

        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signUpButton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}