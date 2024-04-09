package com.example.jobfinder.UI.FindNewJobs

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.R
import com.example.jobfinder.Utils.FragmentHelper
import com.example.jobfinder.databinding.ActivityNewJobBinding

class NewJobActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewJobBinding
    private lateinit var viewModel: FindNewJobViewModel
    lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewJobBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Khởi tạo viewmodel
        viewModel = ViewModelProvider(this)[FindNewJobViewModel::class.java]


        // nút back về home trên màn hình
        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }


        // gán data cứng vào viewmodel và adapter

        viewModel.addJobsData(JobModel("1", "Intern Javascript", "2", "", "", "3", "30000", "", "", "", "01/4/2024", "2", "Công ty TNHH Hoàng Hàu", "", "", "", ""));
        viewModel.addJobsData(JobModel("2", "Senior Java", "2", "", "", "3", "45000", "", "", "", "02/4/2024", "1", "Công ty Nguyên Nguyễn", "", "", "", ""));
        viewModel.addJobsData(JobModel("3", "Intent SQL", "2", "", "", "3", "5000", "", "", "", "03/4/2024", "0", "Tập đoàn Mai Đào", "", "", "", ""));
        viewModel.addJobsData(JobModel("4", "Nhân viên lau bàn", "2", "", "", "3", "2000", "", "", "", "03/4/2024", "1", "Công ty Dũng hót boi", "", "", "", ""));
        viewModel.addJobsData(JobModel("5", "Senior đánh giày", "2", "", "", "3", "123000", "", "", "", "05/4/2024", "2", "Công ty TNHH 10 thành viên", "", "", "", ""));
        viewModel.addJobsData(JobModel("6", "Tạp vụ", "2", "", "", "3", "900000", "", "", "", "08/4/2024", "0", "Công ty Cầu vồng", "", "", "", ""));
        viewModel.addJobsData(JobModel("7", "Tạp vụ 2", "2", "", "", "3", "900000", "", "", "", "08/4/2024", "0", "Công ty Cầu vồng 2", "", "", "", ""));
        viewModel.addJobsData(JobModel("8", "Tạp vụ 3", "2", "", "", "3", "900000", "", "", "", "08/4/2024", "0", "Công ty Cầu vồng 3", "", "", "", ""))



        // Gán danh sách dữ liệu từ ViewModel cho adapter
        val JobsListData = viewModel.getJobsList()
        val adapter = NewJobsAdapter(JobsListData, binding.noDataImage)
        binding.newJobHomeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.newJobHomeRecyclerView.adapter = adapter


        // lắng nghe event được gửi về từ activity đích (activity jobDetail của nhà tuyển dụng (làm sau))
        val startJobDetails = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == RESULT_OK) {
                // Xử lí dữ liệu nhận về nếu cần thiết
            }
        }


        // Click vào từng item trong recycler
        adapter.setOnItemClickListener(object : NewJobsAdapter.onItemClickListener{
            override fun onItemClicked(position: Int) {
//                val intent = Intent(this, JobDetails::class.java)
//                startConfeDetail.launch(intent)
                Toast.makeText(this@NewJobActivity, "Click", Toast.LENGTH_SHORT).show()
            }
        })


        // mục search
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
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

        binding.rootNewJob.setOnClickListener{
            binding.searchView.clearFocus()
        }



        // nút filter
        binding.filterIcon.setOnClickListener{
            // mở drawer
            binding.rootNewJob.openDrawer(GravityCompat.END)

        }



    }


}