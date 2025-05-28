package com.example.coinfin.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.coinfin.R
//import com.example.coinfin.MainActivity

class GoalFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.onboarding_goal, container, false)

        val startButton: Button = view.findViewById(R.id.btnStart)
        startButton.setOnClickListener {
            //val intent = Intent(requireContext(), MainActivity::class.java)
            //startActivity(intent)
            requireActivity().finish() // Cierra el onboarding
        }

        return view
    }
}
