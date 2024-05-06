package com.example.jobfinder.UI.FindNewJobs

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.UI.JobDetails.SeekerJobDetailActivity
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.ActivityNewJobBinding
import com.example.jobfinder.databinding.CustomFilterLayoutBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NewJobActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewJobBinding
    lateinit var binding2: CustomFilterLayoutBinding
    private val viewModel: FindNewJobViewModel by viewModels()
    private lateinit var adapter: NewJobsAdapter
    private var isJobDetailActivityOpen = false
    private val REQUEST_CODE_APPLY_JOB = 1003
    private lateinit var drawerLayout: DrawerLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewJobBinding.inflate(layoutInflater)
        binding2 = CustomFilterLayoutBinding.bind(binding.slideMenu.root)

        setContentView(binding.root)

        // nút back về home trên màn hình
        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        // chạy hàm lấy data các công việc
        if (viewModel.getJobsList().isEmpty()) { fetchJobs() }


        // gán data vào adapter sau khi fetch
        adapter = NewJobsAdapter(viewModel.getJobsList(), binding.noDataImage)
        binding.newJobHomeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.newJobHomeRecyclerView.adapter = adapter


        viewModel.jobsListLiveData.observe(this) { newItem ->
            newItem?.let {
                adapter.updateData(newItem) // Cập nhật adapter khi có dữ liệu mới từ ViewModel
            }
        }

        // Hiển thị hoặc ẩn animationView dựa vào trạng thái loading
        viewModel._isLoading.observe(this) { isLoading ->
            binding.animationView.visibility = if (isLoading) View.VISIBLE else View.GONE
        }


        // lắng nghe event được gửi về từ activity đích (activity jobDetail của nhà tuyển dụng (làm sau))
        val startJobDetails = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                // Xử lí dữ liệu nhận về nếu cần thiết
            }
            isJobDetailActivityOpen = false
        }


        // Click vào từng item trong recycler
        adapter.setOnItemClickListener(object : NewJobsAdapter.onItemClickListener {
            override fun onItemClicked(Job: JobModel) {
                if (!isJobDetailActivityOpen) {
                    isJobDetailActivityOpen = true
                    val intent = Intent(this@NewJobActivity, SeekerJobDetailActivity::class.java)
                    intent.putExtra("job", Job)
                    startActivityForResult(intent, REQUEST_CODE_APPLY_JOB)
                }
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
                    adapter.resetOriginalList()
                    false
                } else { // có nhập text
                    adapter.filter.filter(dataInput)
                    true
                }
            }
        })


        // nút close của searchView
        binding.searchView.setOnCloseListener {
            adapter.resetOriginalList()
            false
        }

        binding.rootNewJob.setOnClickListener {
            binding.searchView.clearFocus()
        }


        // nút filter
        binding.filterIcon.setOnClickListener {
            binding.rootNewJob.openDrawer(GravityCompat.END)

        }

        // nút của drawer

        binding2.JTAll.setOnClickListener {
            Toast.makeText(this, "Button clickedddddd", Toast.LENGTH_SHORT).show()
        }

        binding2.recAll.setOnClickListener {
            Toast.makeText(this@NewJobActivity, "Button clickedddddd2222", Toast.LENGTH_SHORT).show()
        }


    }

    private fun fetchJobs() {
        viewModel._isLoading.value = true
        viewModel.clearJobsList() // xóa item cũ đi trước khi fetch lại
        FirebaseDatabase.getInstance().getReference("Job")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (userSnapshot in dataSnapshot.children) {
                        val buserId = userSnapshot.key.toString()
                        val tempList: MutableList<JobModel> = mutableListOf()
                        GetData.getUsernameFromUserId(buserId) { username ->
                            for (jobSnapshot in userSnapshot.children) {
                                val jobModel = jobSnapshot.getValue(JobModel::class.java)
                                jobModel?.let {
                                    it.BUserName = username.toString()
                                    it.status = GetData.getStatus(it.startTime.toString(), it.endTime.toString(), it.empAmount.toString(), it.numOfRecruited.toString())

                                    tempList.add(it) //Chứa full data toàn bộ các job

                                    if (it.status == "recruiting") { // check trạng thái công việc cho vào viewmodel để hiển thị
                                        viewModel.addJobsToJobsList(it)
                                    }
                                }
                            }
                            viewModel.updateStatusToFirebase(buserId,tempList)
                            Log.d("sdkjdfhdkssd", buserId)
                        }
                    }
                    viewModel._isLoading.value = false
                }
                override fun onCancelled(databaseError: DatabaseError) {
                    viewModel._isLoading.value = false
                }
            })
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_APPLY_JOB && resultCode == Activity.RESULT_OK) {
            isJobDetailActivityOpen = false
        }
    }

}