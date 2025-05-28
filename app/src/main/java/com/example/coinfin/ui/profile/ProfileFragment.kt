package com.example.coinfin.ui.profile

import android.content.Intent
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
import com.example.coinfin.databinding.FragmentProfileBinding
import com.example.coinfin.ui.onboarding.InitialActivity
import com.example.coinfin.utils.AuthManager
import com.example.coinfin.utils.FirestoreManager

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        val user = AuthManager.getCurrentUser()
        user?.let {
            FirestoreManager.getUserData(it.uid) { data ->
                data?.let {
                    binding.usernameText.text = it["username"]?.toString() ?: "N/A"
                    binding.emailText.text = it["email"]?.toString() ?: "N/A"
                }
            }
        }

        binding.logoutButton.setOnClickListener {
            AuthManager.signOut()

            val intent = Intent(requireContext(), InitialActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        return binding.root
    }
}
