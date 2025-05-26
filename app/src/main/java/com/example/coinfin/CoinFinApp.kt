package com.example.coinfin

import android.app.Application
import com.google.firebase.FirebaseApp

class CoinFinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}
