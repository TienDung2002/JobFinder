package com.example.jobfinder.UI.PostedJob

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.UI.JobDetails.JobDetailActivity
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.ActivityPostedJobBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PostedJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostedJobBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostedJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        val uid = auth.currentUser?.uid

        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        val postedJobList: MutableList<JobModel> = mutableListOf()

        FirebaseDatabase.getInstance()
            .getReference("Job")
            .child(uid.toString())
            .get()
            .addOnSuccessListener { data ->
                for(job in data.children){
                    val jobId = job.child("jobId").getValue(String::class.java)
                    val jobTitle = job.child("jobTitle").getValue(String::class.java)
                    val empAmount = job.child("empAmount").getValue(String::class.java)
                    val shift = job.child("shift").getValue(String::class.java)
                    val startTime = job.child("startTime").getValue(String::class.java)
                    val endTime = job.child("endTime").getValue(String::class.java)
                    val salaryPerEmp = job.child("salaryPerEmp").getValue(String::class.java)
                    val address = job.child("address").getValue(String::class.java)
                    val jobDes = job.child("jobDes").getValue(String::class.java)
                    val totalSalary = job.child("totalSalary").getValue(String::class.java)
                    val postDate = job.child("postDate").getValue(String::class.java)
                    val numOfRecruited = job.child("numOfRecruited").getValue(String::class.java)
                    val bUserName = job.child("buserName").getValue(String::class.java)

                    val jobModel = JobModel(
                        jobId,
                        jobTitle,
                        shift,
                        startTime,
                        endTime,
                        empAmount,
                        salaryPerEmp,
                        address,
                        jobDes,
                        totalSalary,
                        postDate,
                        numOfRecruited,
                        bUserName
                    )

                    postedJobList.add(jobModel)

                }
                val sortedPostedJobList = postedJobList.sortedByDescending { GetData.convertStringToDate( it.postDate.toString()) }
                val adapter = PostedJobAdapter(this, sortedPostedJobList)
                // Đoạn mã sau khi khởi tạo adapter
                adapter.setOnItemClickListener(object : PostedJobAdapter.OnItemClickListener {
                    override fun onItemClick(job: JobModel) {
                        // Chuyển sang JobDetailActivity và truyền dữ liệu của công việc
                        val intent = Intent(this@PostedJobActivity, JobDetailActivity::class.java)
                        intent.putExtra("job", job)
                        startActivity(intent)
                    }
                })

                checkEmptyAdapter(postedJobList)
                // Set adapter cho RecyclerView
                binding.recyclerPostedJob.adapter = adapter
                binding.recyclerPostedJob.layoutManager = LinearLayoutManager(this)
                binding.animationView.visibility = View.GONE
            }
    }

    private fun checkEmptyAdapter(list: MutableList<JobModel>) {
        if (list.isEmpty()) {
            binding.noJob.visibility = View.VISIBLE
        } else {
            binding.noJob.visibility = View.GONE
        }
    }
}
