package com.example.jobfinder.UI.PostedJob

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.UI.JobDetails.RecruiterJobDetailActivity
import com.example.jobfinder.databinding.ActivityPostedJobBinding
import com.google.firebase.auth.FirebaseAuth

class PostedJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPostedJobBinding
    private val viewModel: PostedJobViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostedJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener {
            setResult(RESULT_OK)
            finish()
        }
        viewModel.fetchPostedJobs()
        viewModel.postedJobList.observe(this) { postedJobList ->
            val adapter = PostedJobAdapter(this, postedJobList)
            adapter.setOnItemClickListener(object : PostedJobAdapter.OnItemClickListener {
                override fun onItemClick(job: JobModel) {
                    val intent = Intent(this@PostedJobActivity, RecruiterJobDetailActivity::class.java)
                    intent.putExtra("job", job)
                    startActivity(intent)
                    finish()
                }
            })
            checkEmptyAdapter(postedJobList)
            binding.recyclerPostedJob.adapter = adapter
            binding.recyclerPostedJob.layoutManager = LinearLayoutManager(this)
            binding.animationView.visibility = View.GONE
        }
    }

    private fun checkEmptyAdapter(list: List<JobModel>) {
        if (list.isEmpty()) {
            binding.noJob.visibility = View.VISIBLE
        } else {
            binding.noJob.visibility = View.GONE
        }
    }

}