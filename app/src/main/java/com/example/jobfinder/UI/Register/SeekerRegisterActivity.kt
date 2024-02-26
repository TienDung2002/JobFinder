package com.example.jobfinder.UI.Register

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobfinder.databinding.ActivitySeekerRegisterBinding

class SeekerRegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivitySeekerRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeekerRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // trở về login
        binding.returnbackLogin.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}