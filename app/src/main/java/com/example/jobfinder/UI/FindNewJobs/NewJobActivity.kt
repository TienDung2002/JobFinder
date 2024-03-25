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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.newJobHomeData
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivityNewJobBinding

class NewJobActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewJobBinding
    private lateinit var viewModel: FindNewJobViewModel


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
        viewModel.addJobsData(newJobHomeData("1", "Công ty TNHH Dục", "Intern Javascript", "", "", "", "3", "1", "160000", "", "", "", "25/3/2024" ))
        viewModel.addJobsData(newJobHomeData("2", "Công ty TNHH Hoằng", "Fresher Java", "", "", "", "3", "1", "100000", "", "", "", "26/3/2024" ))
        viewModel.addJobsData(newJobHomeData("3", "Công ty TNHH Mai", "Senior SQL", "", "", "", "3", "1", "240000", "", "", "", "27/3/2024" ))
        viewModel.addJobsData(newJobHomeData("4", "Công ty Xi măng Nguyễn", "Trộn vữa", "", "", "", "3", "1", "64000", "", "", "", "28/3/2024" ))
        viewModel.addJobsData(newJobHomeData("5", "Công ty TNHH Dục", "Intern Javascript", "", "", "", "3", "1", "160000", "", "", "", "29/3/2024" ))
        viewModel.addJobsData(newJobHomeData("6", "Công ty TNHH Hoằng", "Fresher Java", "", "", "", "3", "1", "100000", "", "", "", "29/3/2024" ))
        viewModel.addJobsData(newJobHomeData("7", "Công ty TNHH Mai", "Senior SQL", "", "", "", "3", "1", "240000", "", "", "", "27/3/2024" ))
        viewModel.addJobsData(newJobHomeData("8", "Công ty Xi măng Nguyễn", "Trộn vữa", "", "", "", "3", "1", "64000", "", "", "", "28/3/2024" ))

        // Gán danh sách dữ liệu từ ViewModel cho adapter
        val JobsListData = viewModel.getJobsList()
        var adapter = NewJobsAdapter(JobsListData, binding.noDataImage)
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
                if (dataInput.isEmpty()) {
                    adapter.resetOriginalList()
                    return false
                } else { // có nhập text
                    adapter.filter.filter(dataInput)
                    return true
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

    }


}