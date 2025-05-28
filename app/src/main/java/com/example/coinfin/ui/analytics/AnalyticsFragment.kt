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

class AnalyticsFragment : Fragment() {

    private lateinit var binding: FragmentAnalyticsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnalyticsBinding.inflate(inflater, container, false)

        val user = AuthManager.getCurrentUser()
        user?.let {
            FirestoreManager.getUserData(it.uid) { data ->
                data?.let {
                    val gastos = it["monthly_expense"] ?: 0
                    val ahorro = it["monthly_saving"] ?: 0
                    binding.expenseText.text = "Expenses: €$gastos"
                    binding.savingText.text = "Savings: €$ahorro"
                }
            }
        }

        return binding.root
    }
}
