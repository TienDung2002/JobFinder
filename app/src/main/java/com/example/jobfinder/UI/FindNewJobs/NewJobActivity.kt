package com.example.jobfinder.UI.FindNewJobs

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        viewModel.addJobsData(newJobHomeData("1", R.drawable.avatar, "Công ty TNHH 10 thành viên", "Intern Javascript", "3", "2", "23/3/2023", "20000" ))
        viewModel.addJobsData(newJobHomeData("2", R.drawable.app_icon,"Công ty TNHH 10 thành viên2", "Intern Javascript2", "3", "2", "23/3/2023", "15000" ))
        viewModel.addJobsData(newJobHomeData("3", R.drawable.add,"Công ty TNHH 10 thành viên3", "Intern Javascript3", "3", "2", "23/3/2023", "8000" ))
        viewModel.addJobsData(newJobHomeData("4", R.drawable.app_icon_ct2,"Công ty TNHH 10 thành viên4", "Intern Javascript4", "3", "2", "23/3/2023", "100000" ))
        viewModel.addJobsData(newJobHomeData("5", R.drawable.avatar, "Công ty TNHH 10 thành viên", "Intern Javascript", "3", "2", "23/3/2023", "20000" ))
        viewModel.addJobsData(newJobHomeData("6", R.drawable.app_icon,"Công ty TNHH 10 thành viên2", "Intern Javascript2", "3", "2", "23/3/2023", "15000" ))
        viewModel.addJobsData(newJobHomeData("7", R.drawable.add,"Công ty TNHH 10 thành viên3", "Intern Javascript3", "3", "2", "23/3/2023", "8000" ))
        viewModel.addJobsData(newJobHomeData("8", R.drawable.app_icon_ct2,"Công ty TNHH 10 thành viên4", "Intern Javascript4", "3", "2", "23/3/2023", "100000" ))


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




    }


}