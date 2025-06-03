package com.example.coinfin.data.repository

import com.example.coinfin.data.models.Gasto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreGastoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    fun guardarGasto(gasto: Gasto) {
        db.collection("users")
            .document(userId)
            .collection("gastos")
            .add(gasto)
    }

    fun obtenerGastos(onResult: (List<Gasto>) -> Unit) {
        db.collection("users")
            .document(userId)
            .collection("gastos")
            .get()
            .addOnSuccessListener { snapshot ->
                val lista = snapshot.mapNotNull { it.toObject(Gasto::class.java) }
                onResult(lista)
            }
    }

    fun obtenerObjetivo(onResult: (Double) -> Unit) {
        db.collection("users")
            .document(userId)
            .collection("objetivos")
            .document("mensual")
            .get()
            .addOnSuccessListener {
                val cantidad = it.getDouble("cantidad") ?: 1000.0
                onResult(cantidad)
            }
    }
}
