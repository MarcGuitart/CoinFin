package com.example.coinfin.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coinfin.data.models.CategoriaGasto
import com.example.coinfin.data.models.Gasto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class HomeViewModel(private val uid: String) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _gastos = MutableLiveData<List<Gasto>>()
    val gastos: LiveData<List<Gasto>> = _gastos

    private val _objetivo = MutableLiveData<Double>()
    val objetivo: LiveData<Double> = _objetivo

    private val _progresoMensual = MutableLiveData<Int>()
    val progresoMensual: LiveData<Int> = _progresoMensual

    init {
        cargarConfiguracion()
    }

    private fun cargarConfiguracion() {
        // 1) Primero leo la configuración (objetivo)
        db.collection("users").document(uid)
            .collection("reglas").document("config")
            .get()
            .addOnSuccessListener { doc ->
                val ahorro = doc.getLong("ahorro_mensual")?.toDouble() ?: 0.0
                _objetivo.value = ahorro
                cargarGastos()  // luego cargo los gastos y calculo progreso
            }
            .addOnFailureListener {
                _objetivo.value = 0.0
                cargarGastos()
            }
    }

    private fun cargarGastos() {
        db.collection("users").document(uid)
            .collection("gastos")
            .orderBy("fecha", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { snap ->
                val lista = snap.mapNotNull { it.toObject(Gasto::class.java) }
                _gastos.value = lista.take(3)
                calcularProgreso(lista.sumOf { it.cantidad })
            }
    }

    private fun calcularProgreso(totalGasto: Double) {
        val obj = _objetivo.value ?: 0.0
        val pct = if (obj > 0) ((totalGasto / obj) * 100).toInt().coerceIn(0, 200) else 0
        _progresoMensual.value = pct
    }
}

