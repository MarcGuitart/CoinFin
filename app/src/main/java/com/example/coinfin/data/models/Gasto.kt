package com.example.coinfin.data.models

data class Gasto(
    val categoria: String,
    val monto: String,
    val fecha: String,
    val alerta: Boolean = false
)
