package com.example.coinfin.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coinfin.adapter.GastoAdapter
import com.example.coinfin.databinding.FragmentHomeBinding
import com.example.coinfin.data.models.Gasto
import com.example.coinfin.utils.AuthManager
import com.example.coinfin.utils.FirestoreManager
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var ahorroObjetivo: Int = 300
    private var ahorroActual: Int = 263

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupHeader()
        setupGastosList()
        checkAlertaDesviacion()

        binding.configurarObjetivoBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Abrir configuración de objetivos", Toast.LENGTH_SHORT).show()
            // Aquí puedes abrir otra vista
        }

        return binding.root
    }

    private fun setupHeader() {
        val diferencia = ahorroObjetivo - ahorroActual
        val texto = if (diferencia > 0) {
            binding.diferenciaText.setTextColor(Color.parseColor("#4CAF50"))
            "Estás a ${diferencia}€ de tu objetivo mensual"
        } else {
            binding.diferenciaText.setTextColor(Color.RED)
            "Has sobrepasado tu objetivo por ${-diferencia}€"
        }
        binding.diferenciaText.text = texto
    }

    private fun setupGastosList() {
        val formatter = SimpleDateFormat("dd MMM", Locale.getDefault())
        val gastos = listOf(
            Gasto("Cafetería Starbucks", "Ocio", formatter.format(Date()), true),
            Gasto("Glovo Hamburguesa", "Delivery", formatter.format(Date()), true),
            Gasto("Taxi noche", "Transporte", formatter.format(Date()), false)
        )

        val adapter = GastoAdapter(gastos)
        binding.recyclerGastos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerGastos.adapter = adapter
    }

    private fun checkAlertaDesviacion() {
        if (ahorroActual < ahorroObjetivo * 0.8) {
            binding.alertCard.visibility = View.VISIBLE
        } else {
            binding.alertCard.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
