package com.example.coinfin.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.example.coinfin.databinding.FragmentProfileBinding
import com.example.coinfin.ui.onboarding.InitialActivity
import com.example.coinfin.utils.AuthManager
import com.example.coinfin.utils.FirestoreManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private var ahorroObjetivo = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        setupInputs()
        val uid = AuthManager.getCurrentUser()?.uid ?: return binding.root

        FirestoreManager.obtenerConfiguracionUsuario(uid) { config ->
            config?.let {
                binding.notificationSwitch.isChecked = it.notificaciones
                binding.chipRule1.isChecked = it.reglasAhorro
            }
        }
        setupListeners()

        return binding.root
    }

    private fun setupInputs() {
        // Configurar NumberPicker
        binding.numberPicker.minValue = 1
        binding.numberPicker.maxValue = 31
        binding.CerrarSesionButton.setOnClickListener {
            AuthManager.signOut() // Cierra sesión (debes tener este método en tu AuthManager)
            val intent = Intent(requireContext(), InitialActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun setupListeners() {
        val user = AuthManager.getCurrentUser()
        var diaSeleccionado = 1

        binding.metaEditText.addTextChangedListener { editable ->
            ahorroObjetivo = editable.toString().toIntOrNull() ?: 0
            updateImpactPreview()

            user?.let {
                FirestoreManager.guardarConfiguracionUsuario(
                    uid = user.uid,
                    ahorroMensual = ahorroObjetivo,
                    diaInicioMes = diaSeleccionado,
                    notificaciones = true
                ) { exito ->
                    if (!exito) Toast.makeText(requireContext(), "Error guardando configuración", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.numberPicker.setOnValueChangedListener { _, _, newVal ->
            diaSeleccionado = newVal

            user?.let {
                FirestoreManager.guardarRegla(user.uid, diaSeleccionado) { exito ->
                    if (!exito) Toast.makeText(requireContext(), "Error guardando regla", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            val uid = AuthManager.getCurrentUser()?.uid ?: return@setOnCheckedChangeListener
            val data = mapOf("notificaciones" to isChecked)
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("reglas")
                .document("config")
                .set(data, SetOptions.merge())
        }

        binding.chipRule1.setOnCheckedChangeListener { _, isChecked ->
            val uid = AuthManager.getCurrentUser()?.uid ?: return@setOnCheckedChangeListener
            val data = mapOf("reglas_ahorro" to isChecked)
            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("reglas")
                .document("config")
                .set(data, SetOptions.merge())
        }

        binding.categoriasNecesariasEditText.addTextChangedListener {
            val texto = it.toString()
            val lista = texto.split(",").map { cat -> cat.trim().lowercase() }.filter { cat -> cat.isNotEmpty() }

            val uid = AuthManager.getCurrentUser()?.uid ?: return@addTextChangedListener
            val data = mapOf("categorias_evitables" to lista)

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .collection("reglas")
                .document("config")
                .set(data, SetOptions.merge())
        }
    }


    private fun updateImpactPreview() {
        val impacto = ahorroObjetivo + 45
        binding.metaEditText.addTextChangedListener { editable ->
            val objetivo = editable.toString().toIntOrNull() ?: 0
            binding.impacto.text = "Tu objetivo es ahorrar €$objetivo cada mes."
        }
        val tips = listOf(
            "Lleva agua de casa en lugar de comprar botella",
            "Prepara tu café en casa antes de salir",
            "Revisa suscripciones y cancela las que no uses"
        )
        binding.tipTextView.text = tips.random()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
