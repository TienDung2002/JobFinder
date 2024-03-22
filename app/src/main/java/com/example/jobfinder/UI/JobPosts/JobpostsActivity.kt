package com.example.jobfinder.UI.JobPosts

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivityHomeBinding
import com.example.jobfinder.databinding.ActivityJobpostsBinding

class JobpostsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobpostsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobpostsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //back btn
        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

}