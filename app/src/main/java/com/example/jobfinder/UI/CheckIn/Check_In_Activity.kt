package com.example.jobfinder.UI.CheckIn

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.AppliedJobModel
import com.example.jobfinder.databinding.ActivityCheckInBinding

class Check_In_Activity : AppCompatActivity() {
    private lateinit var binding: ActivityCheckInBinding
    private val viewModel: CheckInViewModel by viewModels()
    private lateinit var adapter: CheckInAdapter
    private val REQUEST_CODE = 1002
    private var isActivityOpened = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animationView.visibility = View.VISIBLE

        // Tạo adapter và gán vào RecyclerView
        adapter = CheckInAdapter(mutableListOf(), binding.root.context)
        binding.recyclerCheckIn.adapter = adapter
        binding.recyclerCheckIn.layoutManager = LinearLayoutManager(this)

        viewModel.ApprovedJobList.observe(this) { updatedList ->
            adapter.updateData(updatedList)
            checkEmptyAdapter(updatedList)
        }

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