// AnalyticsViewModel.kt
package com.example.coinfin.ui.analytics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coinfin.data.models.Gasto
import com.example.coinfin.utils.FirestoreManager
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AnalyticsViewModel(private val uid: String) : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    private val _reglas = MutableLiveData<FirestoreManager.ConfigReglas>()
    val reglas: LiveData<FirestoreManager.ConfigReglas> = _reglas

    private val _gastos = MutableLiveData<List<Gasto>>()
    val gastos: LiveData<List<Gasto>> get() = _gastos


    init {
        cargarReglasYDatos()
    }

    private fun cargarReglasYDatos() {
        db.collection("users").document(uid).collection("reglas").document("config")
            .get()
            .addOnSuccessListener { doc ->
                val config = FirestoreManager.ConfigReglas(
                    ahorroMensual = doc.getLong("ahorro_mensual")?.toInt() ?: 0,
                    diaInicioMes = doc.getLong("dia_inicio_mes")?.toInt() ?: 1,
                    notificaciones = doc.getBoolean("notificaciones") ?: true,
                    reglasAhorro = doc.getBoolean("reglas_ahorro") ?: true
                )
                _reglas.value = config
                cargarGastos(config.diaInicioMes)
            }
    }

    private fun cargarGastos(diaInicio: Int) {

        FirestoreManager.obtenerCategoriasEvitables(uid) { categoriasEvitables ->
            db.collection("users").document(uid)
                .collection("gastos")
                .whereGreaterThanOrEqualTo("fecha", primerTimestampDelMes(diaInicio))
                .get()
                .addOnSuccessListener { snap ->
                    val lista = snap.mapNotNull { doc ->
                        val categoria = doc.getString("categoria")?.lowercase() ?: ""
                        val evitable = categoria in categoriasEvitables
                        Gasto(
                            categoria = categoria,
                            cantidad = doc.getDouble("cantidad") ?: 0.0,
                            fecha = doc.getTimestamp("fecha") ?: Timestamp.now(),
                            alerta = doc.getBoolean("alerta") ?: false,
                            evitable = evitable
                        )
                    }
                    _gastos.value = snap.mapNotNull { it.toObject(Gasto::class.java) }
                }
        }

        db.collection("users").document(uid).collection("gastos")
            .whereGreaterThanOrEqualTo("fecha", primerTimestampDelMes(diaInicio))
            .get()
            .addOnSuccessListener { snap ->
                val lista = snap.mapNotNull { doc ->
                    try {
                        Gasto(
                            categoria = doc.getString("categoria") ?: "",
                            cantidad = doc.getDouble("cantidad") ?: 0.0,
                            fecha = doc.getTimestamp("fecha") ?: Timestamp.now(),
                            alerta = doc.getBoolean("alerta") ?: false
                        )
                    } catch (e: Exception) {
                        null
                    }
                }
                _gastos.value = lista
            }
    }

    private fun primerTimestampDelMes(dia: Int): Timestamp {
        val cal = Calendar.getInstance().apply {
            time = Date()
            if (get(Calendar.DAY_OF_MONTH) < dia) add(Calendar.MONTH, -1)
            set(Calendar.DAY_OF_MONTH, dia)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        return Timestamp(cal.time)
    }
}
