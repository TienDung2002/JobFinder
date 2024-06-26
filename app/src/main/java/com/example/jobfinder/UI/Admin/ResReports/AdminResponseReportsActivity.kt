package com.example.jobfinder.UI.Admin.ResReports

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.SupportUser
import com.example.jobfinder.UI.JobEmpList.JobEmpListActivity
import com.example.jobfinder.databinding.ActivityAdminResponseReportsBinding

class AdminResponseReportsActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdminResponseReportsBinding
    private val viewModel:AdminResReportViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminResponseReportsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // nút back về
        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        val adapter = AdminResReportAdapter(mutableListOf(), binding.noReport)
        binding.recyclerReportList.adapter = adapter
        binding.recyclerReportList.layoutManager = LinearLayoutManager(this)
        binding.animationView.visibility = View.GONE

        viewModel.reportList.observe(this){ reportList->
            adapter.updateData(reportList)
            checkEmptyAdapter(reportList)
        }

        viewModel.fetchReport()

    }

    private fun checkEmptyAdapter(list: MutableList<SupportUser>) {
        if (list.isEmpty()) {
            binding.noReport.visibility = View.VISIBLE
            binding.animationView.visibility = View.GONE
        } else {
            binding.noReport.visibility = View.GONE
            binding.animationView.visibility = View.GONE
        }
    }
}