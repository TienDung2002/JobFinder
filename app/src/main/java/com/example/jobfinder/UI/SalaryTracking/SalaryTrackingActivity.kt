package com.example.jobfinder.UI.SalaryTracking

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.AppliedJobModel
import com.example.jobfinder.UI.CheckIn.CheckInViewModel
import com.example.jobfinder.databinding.ActivityJobsmanagementBinding
import com.example.jobfinder.databinding.ActivitySalaryTrackingBinding
import com.example.jobfinder.databinding.ActivityWorkingJobBinding

class SalaryTrackingActivity : AppCompatActivity() {
//    private lateinit var binding: ActivitySalaryTrackingBinding
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivitySalaryTrackingBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//    }

    private lateinit var binding: ActivityJobsmanagementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobsmanagementBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // back bằng nút trên màn hình
        binding.backButton.setOnClickListener{
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    // back bằng nút hoặc vuốt trên thiết bị
    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Khởi tạo Intent để quay lại HomeActivity
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

}