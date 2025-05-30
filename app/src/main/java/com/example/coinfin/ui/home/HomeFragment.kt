package com.example.coinfin.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coinfin.R
import com.example.coinfin.adapter.GastoAdapter
import com.example.coinfin.data.models.Gasto
import com.example.coinfin.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val mockGastos = listOf(
            Gasto("Café Starbucks", "-3.20€", "27 mayo", true),
            Gasto("Transporte", "-1.50€", "26 mayo"),
            Gasto("Netflix", "-12.99€", "25 mayo")
        )

        binding.recyclerViewExpenses.adapter = GastoAdapter(mockGastos)
        binding.recyclerViewExpenses.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
