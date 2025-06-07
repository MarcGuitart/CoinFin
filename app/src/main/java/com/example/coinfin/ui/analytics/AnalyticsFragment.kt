package com.example.coinfin.ui.analytics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.coinfin.databinding.FragmentAnalyticsBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.example.coinfin.utils.*
import java.util.*

class AnalyticsViewModelFactory(private val uid: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AnalyticsViewModel(uid) as T
    }
}

class AnalyticsFragment : Fragment() {

    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!
    val viewModel: AnalyticsViewModel by viewModels {
        val uid = AuthManager.getCurrentUser()?.uid ?: ""
        AnalyticsViewModelFactory(uid)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.gastos.observe(viewLifecycleOwner) { lista ->
            val evitable = lista.filter { it.evitable }.sumOf { it.cantidad }
            val necesario = lista.filter { !it.evitable }.sumOf { it.cantidad }

            setupPieChart(binding.pieChart, necesario, evitable)

            val reglas = viewModel.reglas.value ?: return@observe
            val diaHoy = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
            val gastadoHoy = lista.sumOf { it.cantidad }
            val diasPasados = diaHoy - reglas.diaInicioMes + 1
            val diasTotales = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
            val promedioDiario = gastadoHoy / diasPasados
            val proyeccionTotal = promedioDiario * diasTotales
            val ahorroEst = reglas.ahorroMensual - proyeccionTotal

            setupLineChart(binding.lineChart, promedioDiario, ahorroEst, diasTotales)
        }
    }

    private fun setupPieChart(chart: PieChart, necesario: Double, evitable: Double) {
        val entries = listOf(
            PieEntry(necesario.toFloat(), "Necesario"),
            PieEntry(evitable.toFloat(), "Evitable")
        )
        val dataSet = PieDataSet(entries, "Gastos").apply {
            colors = ColorTemplate.MATERIAL_COLORS.toList()
            valueTextSize = 14f
        }
        chart.data = PieData(dataSet)
        chart.description = Description().apply { text = "Distribución de gastos" }
        chart.animateY(1000)
        chart.invalidate()
    }

    private fun setupLineChart(chart: LineChart, diario: Double, ahorroEst: Double, dias: Int) {
        val entries = mutableListOf<Entry>()
        for (i in 1..dias) {
            val estimado = i * diario
            val ahorro = viewModel.reglas.value?.ahorroMensual ?: 0
            val progreso = ahorro - estimado
            entries.add(Entry(i.toFloat(), progreso.toFloat()))
        }
        val dataSet = LineDataSet(entries, "Ahorro estimado").apply {
            color = ColorTemplate.getHoloBlue()
            valueTextSize = 12f
        }
        chart.data = LineData(dataSet)
        chart.description = Description().apply { text = "Proyección mensual" }
        chart.animateX(1000)
        chart.invalidate()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
