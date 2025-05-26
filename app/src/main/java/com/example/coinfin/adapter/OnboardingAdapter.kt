package com.example.coinfin.adapter


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.coinfin.ui.onboarding.*

class OnboardingAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WelcomeFragment()
            1 -> FeaturesFragment()
            2 -> GoalFragment()
            else -> WelcomeFragment()
        }
    }
}


