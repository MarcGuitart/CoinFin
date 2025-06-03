package com.example.coinfin.data.models

import com.google.firebase.Timestamp

data class Gasto(
    val categoria: String = "",
    val cantidad: Double = 0.0,
    val fecha: Timestamp? = null,
    val alerta: Boolean = false
)
