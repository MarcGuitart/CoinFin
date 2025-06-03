package com.example.coinfin.ui.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.coinfin.R
import com.example.coinfin.databinding.FragmentAnalyticsBinding
import com.example.coinfin.utils.AuthManager
import com.example.coinfin.utils.FirestoreManager
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.firestore.FirebaseFirestore

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)

        setupCharts()
        setupInsights()

        val user = AuthManager.getCurrentUser()
        user?.let {
            val uid = AuthManager.getCurrentUser()?.uid ?: return@let

            val userDoc = FirebaseFirestore.getInstance().collection("users").document(uid)
            userDoc.collection("objetivos").document("mensual").get().addOnSuccessListener { objSnap ->
                val ahorro = objSnap.getDouble("monto") ?: 0.0
                binding.savingText.text = "Objetivo ahorro: €${ahorro.toInt()}"

                userDoc.collection("gastos").get().addOnSuccessListener { gastosSnap ->
                    val gastosTotales = gastosSnap.documents.sumOf {
                        it.getDouble("cantidad") ?: 0.0
                    }
                    binding.expenseText.text = "Gasto total: €${gastosTotales.toInt()}"
                }
            }

        }

        return binding.root
    }

    private fun setupCharts() {
        val lineEntries = listOf(
            Entry(1f, 30f),
            Entry(2f, 50f),
            Entry(3f, 70f),
            Entry(4f, 100f),
            Entry(5f, 112f)
        )
        val lineDataSet = LineDataSet(lineEntries, "Ahorro estimado")
        lineDataSet.color = resources.getColor(R.color.british_green, null)
        val lineData = LineData(lineDataSet)
        binding.lineChart.data = lineData
        binding.lineChart.invalidate()

        // Pastel: Gastos por categoría
        val pieEntries = listOf(
            PieEntry(40f, "Alimentación"),
            PieEntry(25f, "Ocio"),
            PieEntry(20f, "Transporte"),
            PieEntry(15f, "Otros")
        )
        val pieDataSet = PieDataSet(pieEntries, "")
        pieDataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
        val pieData = PieData(pieDataSet)
        binding.pieChart.data = pieData
        binding.pieChart.invalidate()
    }

    private fun setupInsights() {
        val insights = listOf(
            "Has reducido un 12% tu gasto en comidas fuera",
            "Gastaste 5 veces en Cafeterías"
        )
        binding.insightsText.text = insights.joinToString("\n• ", prefix = "• ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
