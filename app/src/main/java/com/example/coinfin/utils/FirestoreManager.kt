package com.example.coinfin.utils

import com.google.firebase.firestore.FirebaseFirestore

object FirestoreManager {

    private val db = FirebaseFirestore.getInstance()

    // Función para guardar datos de usuario
    fun saveUserProfile(uid: String, email: String, username: String, callback: (Boolean) -> Unit) {
        val userMap = mapOf(
            "uid" to uid,
            "email" to email,
            "username" to username
        )
        FirebaseFirestore.getInstance().collection("users").document(uid)
            .set(userMap)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }


    // Función para obtener datos de usuario
    fun getUserData(uid: String, callback: (Map<String, Any>?) -> Unit) {
        db.collection("users").document(uid)
            .get()
            .addOnSuccessListener { document ->
                callback(document.data)
            }
            .addOnFailureListener {
                callback(null)
            }
    }
}
