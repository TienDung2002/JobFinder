package com.example.jobfinder.UI.SalaryTracking

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.AppliedJobModel
import com.example.jobfinder.Datas.Model.CheckInFromBUserModel
import com.example.jobfinder.databinding.ActivitySalaryTrackingBinding

class SalaryTrackingActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySalaryTrackingBinding
    private var isActivityOpened = false
    private val viewModel: SalaryTrackingViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySalaryTrackingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // back bằng nút trên màn hình
        binding.backButton.setOnClickListener{
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        val applied_job = intent.getParcelableExtra<AppliedJobModel>("applied_job")

        if(applied_job!= null){
            val adapter = SalaryTrackingAdapter(binding.root.context, mutableListOf(), applied_job)
            binding.recyclerSalaryTrackingList.adapter = adapter
            binding.recyclerSalaryTrackingList.layoutManager = LinearLayoutManager(this)

            viewModel.CheckInList.observe(this) { updatedList ->
                adapter.updateData(updatedList)
                checkEmptyAdapter(updatedList)
            }

            viewModel.fetchCheckIn(applied_job.jobId.toString())
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

    private fun checkEmptyAdapter(list: MutableList<CheckInFromBUserModel>) {
        if (list.isEmpty()) {
            binding.noSalaryTracking.visibility = View.VISIBLE
            binding.animationView.visibility = View.GONE
        } else {
            binding.noSalaryTracking.visibility = View.GONE
            binding.animationView.visibility = View.GONE
        }
    }

}