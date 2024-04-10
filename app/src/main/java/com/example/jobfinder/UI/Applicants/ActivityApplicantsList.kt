package com.example.jobfinder.UI.Applicants

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.applicantModel
import com.example.jobfinder.databinding.ActivityApplicantsListBinding

class ActivityApplicantsList : AppCompatActivity() {
    private lateinit var binding: ActivityApplicantsListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicantsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val job_id = intent.getStringExtra("job_id")

        // Tạo danh sách mẫu các ứng viên
        val applicantList = listOf(
            applicantModel("1", "Applicant 1's description","", job_id.toString()),
            applicantModel("2", "Applicant 2's description", "","Stand Smith"),
            applicantModel("3", "Applicant 3's description Applicant 3's description Applicant 3's description Applicant 3's description", "","Jane Smith"),
            applicantModel("4", "Applicant 4's description", "","Jane Mary"),
            )

        // Tạo adapter và gán vào RecyclerView
        val adapter = ApplicantAdapter(applicantList)
        binding.recyclerApplicantList.adapter = adapter
        binding.recyclerApplicantList.layoutManager = LinearLayoutManager(this)
        binding.animationView.visibility = View.GONE

        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}