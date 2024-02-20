package com.example.jobfinder.UI.Register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobfinder.databinding.ActivitySeekerRegisterBinding

class SeekerRegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivitySeekerRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeekerRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}