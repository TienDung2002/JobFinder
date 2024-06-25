package com.example.jobfinder.UI.Admin.Statistical

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivityAdminStatisticalBinding
import com.example.jobfinder.databinding.ActivityAdminUserManagBinding

class AdminStatisticalActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdminStatisticalBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminStatisticalBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // nút back về
        binding.backbtn.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }



    }
}