package com.example.coinfin.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.coinfin.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var ahorroObjetivo = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupInputs()
        setupListeners()

        return binding.root
    }

    private fun setupInputs() {
        // Configurar NumberPicker
        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 31
    }

    private fun setupListeners() {
        // EditText de objetivo de ahorro
        binding.metaEditText.addTextChangedListener { editable ->
            ahorroObjetivo = editable.toString().toIntOrNull() ?: 0
            updateImpactPreview()
        }

        // Cambios en el NumberPicker
        binding.numberPicker.setOnValueChangedListener { _, _, newVal ->
        }

        // Listener de Chips (reglas)
        binding.rulesChipGroup.setOnCheckedStateChangeListener { _, _ ->
            updateImpactPreview()
        }

        // Switch de notificaciones
        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            val mensaje = if (isChecked) "Notificaciones activadas" else "Notificaciones desactivadas"
            Toast.makeText(requireContext(), mensaje, Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateImpactPreview() {
        val impacto = ahorroObjetivo + 45
        binding.impactPreview.text = "Si cumples esto, puedes ahorrar €$impacto/mes"

        // Suponiendo 500 como máximo de progreso
        val progreso = (impacto * 100 / 500).coerceIn(0, 100)
        binding.progressBar.progress = progreso
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
