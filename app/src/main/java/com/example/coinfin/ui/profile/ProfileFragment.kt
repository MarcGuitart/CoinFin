package com.example.coinfin.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.coinfin.R
import com.example.coinfin.data.repository.ProfileRepository
import com.example.coinfin.data.repository.UserProfile

class ProfileFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val savingsGoal = view.findViewById<EditText>(R.id.inputSavingsGoal)
        val startDay = view.findViewById<EditText>(R.id.inputStartDay)

        val prefs = requireContext().getSharedPreferences("userPrefs", AppCompatActivity.MODE_PRIVATE)

        val profile = UserProfile(
            uid = "usuario123",
            savingsGoal = 300,
            resetDay = 18,
            avoidCategories = listOf("Cafés", "Suscripciones")
        )

        ProfileRepository.saveUserProfile(profile) { success ->
            if (success) {
                Toast.makeText(context, "Perfil guardado", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Error al guardar", Toast.LENGTH_SHORT).show()
            }
        }

        // Cargar datos guardados
        savingsGoal.setText(prefs.getInt("goal", 0).toString())
        startDay.setText(prefs.getInt("startDay", 1).toString())

        // Guardar cuando el usuario cambia
        savingsGoal.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                prefs.edit().putInt("goal", savingsGoal.text.toString().toIntOrNull() ?: 0).apply()
            }
        }

        startDay.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                prefs.edit().putInt("startDay", startDay.text.toString().toIntOrNull() ?: 1).apply()
            }
        }
    }
}

