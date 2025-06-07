package com.example.coinfin.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coinfin.data.models.CategoriaGasto
import com.example.coinfin.data.models.Gasto
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class HomeViewModel(private val uid: String) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _gastos = MutableLiveData<List<Gasto>>()
    val gastos: LiveData<List<Gasto>> get() = _gastos

    private val _categorias = MutableLiveData<List<CategoriaGasto>>()
    val categorias: LiveData<List<CategoriaGasto>> get() = _categorias

    private val _progresoMensual = MutableLiveData<Int>()
    val progresoMensual: LiveData<Int> get() = _progresoMensual

    private var objetivoMensual = 200.0

    init {
        cargarDatosDesdeFirebase()
    }

    private fun cargarDatosDesdeFirebase() {
        db.collection("users")
            .document(uid)
            .collection("gastos")
            .get()
            .addOnSuccessListener { result ->
                val listaGastos = result.mapNotNull { doc ->
                    try {
                        Gasto(
                            categoria = doc.getString("categoria") ?: "",
                            cantidad = doc.getDouble("cantidad") ?: 0.0,
                            fecha = doc.getTimestamp("fecha") ?: Timestamp.now(),
                            alerta = doc.getBoolean("alerta") ?: false
                        )
                    } catch (e: Exception) {
                        Log.e("HomeViewModel", "Error parsing gasto: ${e.message}")
                        null
                    }
                }.sortedByDescending { it.fecha }

                _gastos.value = listaGastos.take(3)

                _categorias.value = listaGastos.groupBy { it.categoria }.map {
                    CategoriaGasto(it.key, it.value.sumOf { g -> g.cantidad })
                }

                val total = listaGastos.sumOf { it.cantidad }
                _progresoMensual.value = if (objetivoMensual > 0) {
                    ((total / objetivoMensual) * 100).toInt().coerceIn(0, 200)
                } else {
                    0
                }
            }
            .addOnFailureListener {
                Log.e("HomeViewModel", "Error al cargar gastos: ${it.message}")
            }
    }
}
