package com.example.jobfinder.UI.Admin.ResReports

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
    private lateinit var adapter: AdminResReportAdapter
    private var isActivityOpened = false

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

        adapter = AdminResReportAdapter(listOf())
        binding.recyclerReportList.adapter = adapter
        binding.recyclerReportList.layoutManager = LinearLayoutManager(this)

        viewModel.fetchReport()

        viewModel.reportList.observe(this){ reportList->
            adapter.updateData(reportList)
            checkEmptyAdapter(reportList)
        }

        adapter.setOnItemClickListener(object : AdminResReportAdapter.OnItemClickListener {
            override fun onItemClick(report: SupportUser) {
                if (!isActivityOpened) {
                    // Mở activity chỉ khi activity chưa được mở
                    val intent = Intent(this@AdminResponseReportsActivity, JobEmpListActivity::class.java)
                    intent.putExtra("report", report)
                    startActivityForResult(intent, 1004)
                    // Đặt biến kiểm tra là đã mở
                    isActivityOpened = true
                }
            }
        })

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1004 && resultCode == Activity.RESULT_OK) {
            isActivityOpened = false
            viewModel.fetchReport()
        }
    }

    private fun checkEmptyAdapter(list: List<SupportUser>) {
        if (list.isEmpty()) {
            binding.noReport.visibility = View.VISIBLE
            binding.animationView.visibility = View.GONE
        } else {
            binding.noReport.visibility = View.GONE
            binding.animationView.visibility = View.GONE
        }
    }
}