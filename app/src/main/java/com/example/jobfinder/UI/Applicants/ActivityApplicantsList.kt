package com.example.jobfinder.UI.Applicants

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.ApplicantsModel
import com.example.jobfinder.UI.UserDetailInfo.NUserDetailInfoActivity
import com.example.jobfinder.databinding.ActivityApplicantsListBinding

class ActivityApplicantsList : AppCompatActivity() {
    private lateinit var binding: ActivityApplicantsListBinding
    private val REQUEST_CODE = 1002
    private var isActivityOpened = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityApplicantsListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.animationView.visibility = View.VISIBLE

        val job_id = intent.getStringExtra("job_id")


        // Tạo danh sách mẫu các ứng viên
        val applicantList = mutableListOf(
            ApplicantsModel("2xcDUDAkHkXB6Hhcj1uCX2fLTEV2", "Applicant 1's description","", job_id.toString()),
            ApplicantsModel("3MpbWhQQPAZSC6xkAGVndW6I3SB2", "Applicant 2's description", "","Stand Smith"),
            ApplicantsModel("3", "Applicant 3's description Applicant 3's description Applicant 3's description Applicant 3's description", "","Jane Smith"),
            ApplicantsModel("4", "Applicant 4's description", "","Jane Mary"),
            )

        checkEmptyAdapter(applicantList)

        // Tạo adapter và gán vào RecyclerView
        val adapter = ApplicantAdapter(applicantList)
        binding.recyclerApplicantList.adapter = adapter
        binding.recyclerApplicantList.layoutManager = LinearLayoutManager(this)
        binding.animationView.visibility = View.GONE

        adapter.setOnItemClickListener(object : ApplicantAdapter.OnItemClickListener {
            override fun onItemClick(applicant: ApplicantsModel) {
                if (!isActivityOpened){
                    val intent = Intent(this@ActivityApplicantsList, NUserDetailInfoActivity::class.java)
                    intent.putExtra("nuser_applicant", applicant)
                    startActivityForResult(intent, REQUEST_CODE)
                    isActivityOpened = true
                }
            }
        })

        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            isActivityOpened = false
        }
    }

    private fun checkEmptyAdapter(list: List<ApplicantsModel>) {
        if (list.isEmpty()) {
            binding.noApplicant.visibility = View.VISIBLE
            binding.animationView.visibility = View.GONE
        } else {
            binding.noApplicant.visibility = View.GONE
            binding.animationView.visibility = View.GONE
        }
    }
}