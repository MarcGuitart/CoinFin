package com.example.coinfin.utils

import com.example.coinfin.data.models.Gasto
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Date

object FirestoreManager {

    private val db = FirebaseFirestore.getInstance()

    fun getUserData(uid: String, onResult: (Map<String, Any>?) -> Unit) {
        db.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                onResult(document.data)
            }
            .addOnFailureListener {
                onResult(null)
            }
    }

    fun getHistorialGastos(uid: String, onResult: (List<Gasto>) -> Unit) {
        val uid = AuthManager.getCurrentUser()?.uid ?: return

        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(uid).collection("gastos")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val categoria = document.getString("categoria") ?: ""
                    val cantidad = document.getDouble("monto") ?: 0.0
                    val fecha = document.getTimestamp("fecha")?.toDate() ?: Date()
                }
            }
            .addOnFailureListener { onResult(emptyList()) }
    }

    fun guardarObjetivoMensual(uid: String, objetivo: Int, dia: Int, onResult: (Boolean) -> Unit) {
        val objetivoData = mapOf(
            "objetivoAhorro" to objetivo,
            "diaObjetivo" to dia
        )
        db.collection("users").document(uid)
            .collection("objetivos").document("mensual")
            .set(objetivoData)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }

    fun updateReglas(uid: String, reglas: Map<String, Any>) {
        db.collection("users").document(uid).update("reglas", reglas)
    }

    fun updateNotificaciones(uid: String, activadas: Boolean) {
        db.collection("users").document(uid).update("notificaciones", activadas)
    }

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
}
