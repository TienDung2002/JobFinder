package com.example.jobfinder.UI.FindNewJobs

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.ActivityNewJobBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewJobActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewJobBinding
    private lateinit var viewModel: FindNewJobViewModel
    private lateinit var adapter: NewJobsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // nút back về home trên màn hình
        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        viewModel = ViewModelProvider(this).get(FindNewJobViewModel::class.java)
        fetchJobs()

        // gán vào adapter
        adapter = NewJobsAdapter(emptyList(), binding.noDataImage)
        binding.newJobHomeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.newJobHomeRecyclerView.adapter = adapter

        // Quan sát list có thay đổi data hay không
        viewModel.postedJobList.observe(this) { updatedList ->
            updatedList?.let {
                adapter.updateData(it)
            }
        }
        // Hiển thị hoặc ẩn animationView dựa vào trạng thái loading
        viewModel._isLoading.observe(this) { isLoading ->
            binding.animationView.visibility = if (isLoading) View.VISIBLE else View.GONE
        }


        // lắng nghe event được gửi về từ activity đích (activity jobDetail của nhà tuyển dụng (làm sau))
        val startJobDetails =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    // Xử lí dữ liệu nhận về nếu cần thiết
                }
            }


        // Click vào từng item trong recycler
        adapter.setOnItemClickListener(object : NewJobsAdapter.onItemClickListener {
            override fun onItemClicked(position: Int) {
//                val intent = Intent(this, JobDetails::class.java)
//                startConfeDetail.launch(intent)
                Toast.makeText(this@NewJobActivity, "Click", Toast.LENGTH_SHORT).show()
            }
        })


        // mục search
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(submitInput: String?): Boolean {
                // Ẩn bàn phím
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)
                // bỏ focus
                binding.searchView.clearFocus()
                // logic search
                adapter.filter.filter(submitInput)
                return true
            }

            override fun onQueryTextChange(dataInput: String): Boolean {
                // Nếu không nhập text vào
                return if (dataInput.isEmpty()) {
                    adapter.resetOriginalList(viewModel.postedJobList)
                    false
                } else { // có nhập text
                    adapter.filter.filter(dataInput)
                    true
                }
            }
        })


        // nút close của searchView
        binding.searchView.setOnCloseListener {
            adapter.resetOriginalList(viewModel.postedJobList)
            false
        }

        binding.rootNewJob.setOnClickListener {
            binding.searchView.clearFocus()
        }


        // nút filter
        binding.filterIcon.setOnClickListener {
            // mở drawer
            binding.rootNewJob.openDrawer(GravityCompat.END)

        }

    }

    override fun onResume() {
        super.onResume()
        fetchJobs()
    }

    private fun fetchJobs() {
        viewModel._isLoading.value = true
        FirebaseDatabase.getInstance().getReference("Job")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val postedJobList: MutableList<JobModel> = mutableListOf()

                    for (userSnapshot in dataSnapshot.children) {
                        for (jobSnapshot in userSnapshot.children) {
                            val jobModel = jobSnapshot.getValue(JobModel::class.java)
                            jobModel?.let {
                                postedJobList.add(it)
                            }
                        }
                    }
                    // Sắp xếp danh sách công việc theo thời gian đăng
                    val sortedPostedJobList = postedJobList.sortedByDescending { GetData.convertStringToDate(it.postDate.toString()) }
                    viewModel.addJobsData(sortedPostedJobList)

                    // không sắp xếp
//                    viewModel.addJobsData(postedJobList)
                    viewModel._isLoading.value = false
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    viewModel._isLoading.value = false
                }
            })
    }

}