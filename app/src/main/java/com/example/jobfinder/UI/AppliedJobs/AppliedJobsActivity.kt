package com.example.jobfinder.UI.AppliedJobs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.AppliedJobModel
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.R
import com.example.jobfinder.UI.FindNewJobs.FindNewJobViewModel
import com.example.jobfinder.UI.FindNewJobs.NewJobsAdapter
import com.example.jobfinder.UI.JobDetails.SeekerJobDetailActivity
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.ActivityAppliedJobsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AppliedJobsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppliedJobsBinding
    private val viewModel: AppliedJobsViewModel by viewModels()
    private lateinit var adapter: AppliedJobsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppliedJobsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // nút back về home trên màn hình
        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        if (viewModel.getAppliedList().isEmpty()){fetchAppliedJobs()}

        adapter = AppliedJobsAdapter(viewModel.getAppliedList(),binding.noDataImage)
        binding.appliedJobRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.appliedJobRecyclerView.adapter = adapter

        viewModel.appliedListLiveData.observe(this) { newItem ->
            newItem?.let {
                adapter.updateData(newItem) // Cập nhật adapter khi có dữ liệu mới từ ViewModel
            }
        }

        binding.animationView.visibility = View.GONE
//      click vào item applied job
//        adapter.setOnItemClickListener(object : AppliedJobsAdapter.onItemClickListener {
//            override fun onItemClicked(AppliedJob: AppliedJobModel) {
//                val intent = Intent(this@AppliedJobsActivity, SeekerJobDetailActivity::class.java)
//                startActivity(intent)
//                // Ẩn hoặc xóa bottom navigation SeekerJobDetailActivity
//
//
//
//            }
//        })

    }

    private fun fetchAppliedJobs(){
        viewModel.clearAppliedList()
        FirebaseDatabase.getInstance().getReference("AppliedJob")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (userSnapshot in dataSnapshot.children) {
                        val tempList: MutableList<AppliedJobModel> = mutableListOf()
                        for (AppliedJobSnapshot in userSnapshot.children) {
                            val AppliedJobModel = AppliedJobSnapshot.getValue(AppliedJobModel::class.java)
                            AppliedJobModel?.let {
                                tempList.add(it)
                                viewModel.addAppliedToAppliedList(it)
                            }
                        }
                    }
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
    }
}