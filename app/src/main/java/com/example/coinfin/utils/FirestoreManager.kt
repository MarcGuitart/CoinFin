package com.example.coinfin.utils

import com.example.coinfin.data.models.Gasto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
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

    fun guardarRegla(uid: String, diaInicioMes: Int, callback: (Boolean) -> Unit) {
        val data = mapOf("diaInicioMes" to diaInicioMes)
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("reglas")
            .document("config")
            .set(data, SetOptions.merge())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
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

    fun obtenerReglaInicioMes(uid: String, callback: (Int?) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("reglas")
            .document("config")
            .get()
            .addOnSuccessListener { doc ->
                val dia = doc.getLong("diaInicioMes")?.toInt()
                callback(dia)
            }
            .addOnFailureListener {
                callback(null)
            }
    }

    fun guardarConfiguracionUsuario(
        uid: String,
        ahorroMensual: Int,
        diaInicioMes: Int,
        notificaciones: Boolean = true,
        callback: (Boolean) -> Unit
    ) {
        val data = mapOf(
            "ahorro_mensual" to ahorroMensual,
            "dia_inicio_mes" to diaInicioMes,
            "notificaciones" to notificaciones,
            "reglas_ahorro" to true
        )

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("reglas")
            .document("config")
            .set(data, SetOptions.merge())
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

    data class ConfigReglas(
        val ahorroMensual: Int = 0,
        val diaInicioMes: Int = 1,
        val notificaciones: Boolean = true,
        val reglasAhorro: Boolean = true
    )

    fun obtenerCategoriasEvitables(uid: String, callback: (List<String>) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("reglas")
            .document("config")
            .get()
            .addOnSuccessListener { doc ->
                val lista = doc.get("categorias_evitables") as? List<String> ?: emptyList()
                callback(lista.map { it.lowercase() })
            }
    }

    fun obtenerConfiguracionUsuario(uid: String, callback: (ConfigReglas?) -> Unit) {
        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("reglas")
            .document("config")
            .get()
            .addOnSuccessListener { doc ->
                val config = ConfigReglas(
                    ahorroMensual = doc.getLong("ahorro_mensual")?.toInt() ?: 0,
                    diaInicioMes = doc.getLong("dia_inicio_mes")?.toInt() ?: 1,
                    notificaciones = doc.getBoolean("notificaciones") ?: true,
                    reglasAhorro = doc.getBoolean("reglas_ahorro") ?: true
                )
                callback(config)
            }
            .addOnFailureListener {
                callback(null)
            }
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
