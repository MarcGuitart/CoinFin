package com.example.coinfin.ui

import com.example.coinfin.adapter.OnboardingAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.coinfin.R
import me.relex.circleindicator.CircleIndicator3



class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val viewPager = findViewById<ViewPager2>(R.id.onboardingViewPager)
        val adapter = OnboardingAdapter(this)
        viewPager.adapter = adapter
    }
}
