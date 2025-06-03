package com.example.coinfin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coinfin.R
import com.example.coinfin.data.models.Gasto

class GastoAdapter(private val lista: List<Gasto>) : RecyclerView.Adapter<GastoAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoria: TextView = view.findViewById(R.id.tvCategoria)
        val monto: TextView = view.findViewById(R.id.tvMonto)
        val fecha: TextView = view.findViewById(R.id.tvFecha)
        val alerta: ImageView = view.findViewById(R.id.ivAlerta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vista = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gasto, parent, false)
        return ViewHolder(vista)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val gasto = lista[position]
        holder.categoria.text = gasto.categoria
        holder.monto.text = "€${gasto.cantidad}"

        val formatter = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
        val fechaFormateada = gasto.fecha?.toDate()?.let { formatter.format(it) } ?: "Sin fecha"
        holder.fecha.text = fechaFormateada

        holder.alerta.visibility = if (gasto.alerta) View.VISIBLE else View.GONE
    }


    override fun getItemCount(): Int = lista.size
}
