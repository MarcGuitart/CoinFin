package com.example.coinfin.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coinfin.data.models.CategoriaGasto
import com.example.coinfin.data.models.Gasto
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

class HomeViewModel(private val uid: String) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _gastos = MutableLiveData<List<Gasto>>()
    val gastos: LiveData<List<Gasto>> get() = _gastos

    private val _categorias = MutableLiveData<List<CategoriaGasto>>()
    val categorias: LiveData<List<CategoriaGasto>> get() = _categorias

    private val _progresoMensual = MutableLiveData<Int>()
    val progresoMensual: LiveData<Int> get() = _progresoMensual

    private val objetivoMensual = 1000.0

    init {
        cargarDatosDesdeFirebase(uid)
    }

    private fun cargarDatosDesdeFirebase(uid: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .document(uid)
            .collection("gastos")
            .get()
            .addOnSuccessListener { result ->
                val gastosFirebase = result.documents.mapNotNull { doc ->
                    doc.toObject(Gasto::class.java)
                }
                _gastos.value = gastosFirebase
                // Otros cálculos...
            }
            .addOnFailureListener { e ->
                Log.e("HomeViewModel", "Error al cargar gastos", e)
            }
    }
}
