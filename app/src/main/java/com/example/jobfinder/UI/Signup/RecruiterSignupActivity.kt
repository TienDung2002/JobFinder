package com.example.jobfinder.UI.Signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivityRecruiterSignupBinding

class RecruiterSignupActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecruiterSignupBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecruiterSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}