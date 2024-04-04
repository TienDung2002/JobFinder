package com.example.jobfinder.UI.UserDetailInfo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.jobfinder.databinding.ActivityBuserDetailInfoBinding

class BUserDetailInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuserDetailInfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuserDetailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val uid = intent.getStringExtra("uid")

        binding.uid.text = uid

        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}