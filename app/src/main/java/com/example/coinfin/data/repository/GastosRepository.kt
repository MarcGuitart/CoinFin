package com.example.coinfin.data.repository

import android.util.Log
import com.example.coinfin.data.models.Gasto
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GastoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()

    fun obtenerGastos(callback: (List<Gasto>) -> Unit) {
        db.collection("users").document(userId).collection("gastos")
            .orderBy("fecha", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { result ->
                val lista = result.mapNotNull { doc ->
                    try {
                        Gasto(
                            categoria = doc.getString("categoria") ?: "",
                            cantidad = doc.getDouble("cantidad") ?: 0.0,
                            fecha = doc.getTimestamp("fecha") ?: Timestamp.now()
                        )
                    } catch (e: Exception) {
                        Log.e("GastoRepo", "Error al parsear gasto: ${e.message}")
                        null
                    }
                }
                callback(lista)
            }
            .addOnFailureListener {
                Log.e("GastoRepo", "Error al leer gastos: ${it.message}")
                callback(emptyList())
            }
    }
}
