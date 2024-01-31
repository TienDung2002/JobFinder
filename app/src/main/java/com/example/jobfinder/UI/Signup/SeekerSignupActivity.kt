package com.example.jobfinder.UI.Signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivitySeekerSignupBinding

class SeekerSignupActivity : AppCompatActivity() {
    lateinit var binding: ActivitySeekerSignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeekerSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}