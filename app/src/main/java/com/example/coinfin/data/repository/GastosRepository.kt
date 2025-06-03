package com.example.coinfin.data.repository

import com.example.coinfin.data.models.Gasto
import com.google.firebase.firestore.FirebaseFirestore

class GastoRepository {

    private val db = FirebaseFirestore.getInstance()
    private val gastosCollection = db.collection("gastos")

    fun obtenerGastosDelUsuario(uid: String, onResult: (List<Gasto>) -> Unit) {
        gastosCollection.whereEqualTo("userId", uid)
            .get()
            .addOnSuccessListener { snapshot ->
                val gastos = snapshot.documents.mapNotNull { it.toObject(Gasto::class.java) }
                onResult(gastos)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }
}
