package com.example.coinfin.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

object AuthManager {
    private val auth = FirebaseAuth.getInstance()

    // PRE: Inicia sesión con email y password usando Firebase Auth
    // POST: Devuelve éxito o error y redirige si es exitoso
    fun signIn(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                callback(it.isSuccessful, if (it.isSuccessful) null else it.exception?.message)
            }
    }

    fun signUp(email: String, password: String, callback: (Boolean, FirebaseUser?, String?) -> Unit) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                val user = FirebaseAuth.getInstance().currentUser
                callback(task.isSuccessful, user, task.exception?.message)
            }
    }


    fun getCurrentUser(): FirebaseUser? = auth.currentUser
    fun signOut() = auth.signOut()
}
