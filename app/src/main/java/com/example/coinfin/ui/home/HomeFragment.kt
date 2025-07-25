package com.example.coinfin.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coinfin.R
import com.example.coinfin.adapter.GastoAdapter
import com.example.coinfin.data.models.Gasto
import com.example.coinfin.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        HomeViewModelFactory(uid)
    }
    private var ahorroObjetivo: Int = 300
    private var ahorroActual: Int = 263

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        setupObservers()
        setupListeners()
        setupHeader()
        setupGastosList()  // Si quieres mantener los datos de prueba

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
        val gastos = listOf(
            Gasto("Cafetería Starbucks", 23.0, Timestamp.now(), true),
            Gasto("Glovo Hamburguesa", 53.0, Timestamp.now(), true),
            Gasto("Taxi noche", 5.0, Timestamp.now(), false)
        )

        val adapter = GastoAdapter(gastos)
        binding.recyclerGastos.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerGastos.adapter = adapter
    }

    private fun checkAlertaDesviacion(progreso: Int) {
        binding.alertCard.visibility = if (progreso < 80) View.VISIBLE else View.GONE
    }

    private fun setupObservers() {
        viewModel.gastos.observe(viewLifecycleOwner) { gastos ->
            val adapter = GastoAdapter(gastos)
            binding.recyclerGastos.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerGastos.adapter = adapter
        }

        viewModel.progresoMensual.observe(viewLifecycleOwner) { pct ->
            binding.progresoMensual.setProgressCompat(pct, true)
            // actualiza el texto de diferencia si lo haces con % o €:
            binding.diferenciaText.text = if (pct < 100)
                "Estás a ${100 - pct}% de tu objetivo"
            else
                "¡Objetivo alcanzado!"
        }
    }

    private fun setupListeners() {
        binding.configurarObjetivoBtn.setOnClickListener {
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView).selectedItemId = R.id.nav_profile
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
