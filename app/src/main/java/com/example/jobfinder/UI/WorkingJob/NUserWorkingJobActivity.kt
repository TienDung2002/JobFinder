package com.example.jobfinder.UI.WorkingJob

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
import com.example.jobfinder.UI.SalaryTracking.SalaryTrackingActivity
import com.example.jobfinder.databinding.ActivityWorkingJobBinding

class NUserWorkingJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWorkingJobBinding
    private val viewModel: CheckInViewModel by viewModels()
    private lateinit var adapter: NUserWorkingJobAdapter
    private val REQUEST_CODE = 1002
    private var isActivityOpened = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkingJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.animationView.visibility = View.VISIBLE

        // Tạo adapter và gán vào RecyclerView
        adapter = NUserWorkingJobAdapter(binding.root.context, mutableListOf())
        binding.recyclerWorkingJob.adapter = adapter
        binding.recyclerWorkingJob.layoutManager = LinearLayoutManager(this)

        viewModel.ApprovedJobList.observe(this) { updatedList ->
            adapter.updateData(updatedList)
            checkEmptyAdapter(updatedList)
        }

        adapter.setOnItemClickListener(object : NUserWorkingJobAdapter.OnItemClickListener {
            override fun onItemClick(job: AppliedJobModel) {
                if (!isActivityOpened) {
                    // Mở activity chỉ khi activity chưa được mở
                    val intent = Intent(this@NUserWorkingJobActivity, SalaryTrackingActivity::class.java)
                    intent.putExtra("applied_job", job)
                    startActivityForResult(intent, REQUEST_CODE)
                    // Đặt biến kiểm tra là đã mở
                    isActivityOpened = true
                }
            }
        })

        viewModel.fetchApprovedJob()

        binding.backButton.setOnClickListener {
            sendResultAndFinish()
        }


    }

    private fun sendResultAndFinish() {
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }


    @Deprecated("Deprecated in Java")
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        sendResultAndFinish()
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            isActivityOpened = false
        }
    }

    private fun checkEmptyAdapter(list: MutableList<AppliedJobModel>) {
        if (list.isEmpty()) {
            binding.noJob.visibility = View.VISIBLE
            binding.animationView.visibility = View.GONE
        } else {
            binding.noJob.visibility = View.GONE
            binding.animationView.visibility = View.GONE
        }
    }
}