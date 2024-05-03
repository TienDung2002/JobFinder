package com.example.jobfinder.UI.FindNewJobs

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.R
import com.example.jobfinder.UI.JobDetails.SeekerJobDetailActivity
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.ActivityNewJobBinding
import com.example.jobfinder.databinding.CustomFilterLayoutBinding
import com.google.android.material.slider.RangeSlider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.NumberFormat
import java.util.Currency

class NewJobActivity : AppCompatActivity() {
    lateinit var binding: ActivityNewJobBinding
    lateinit var cusBindingFilter: CustomFilterLayoutBinding
    private val viewModel: FindNewJobViewModel by viewModels()
    private lateinit var adapter: NewJobsAdapter
    private var isJobDetailActivityOpen = false
    private val REQUEST_CODE_APPLY_JOB = 1003


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewJobBinding.inflate(layoutInflater)
        cusBindingFilter = CustomFilterLayoutBinding.bind(binding.slideMenu.root)
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

        // Cập nhật adapter khi có dữ liệu mới từ ViewModel
        viewModel.jobsListLiveData.observe(this) { newItem ->
            newItem?.let {
                adapter.updateData(newItem)
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







        // PHẦN CUSTOM FILTER
        val jtButtons = listOf(cusBindingFilter.JTAll, cusBindingFilter.JTAtoZ)
        val recNameButtons = listOf(cusBindingFilter.recAll, cusBindingFilter.recAtoZ)
        val postedTimeButtons = listOf(cusBindingFilter.PTAnytime, cusBindingFilter.PTNewest, cusBindingFilter.PTThisMonth)
        val workShiftButtons = listOf(cusBindingFilter.WSAll, cusBindingFilter.WSMorning, cusBindingFilter.WSAfternoon)

        // nút mở filter drawer
        binding.filterIcon.setOnClickListener {
            binding.rootNewJob.openDrawer(GravityCompat.END)
            defaultSelectionFilterUI()
        }

        // Jobtitle btn list
        jtButtons.forEach { button ->
            button.setOnClickListener {
                handleButtonSelection(jtButtons, button)
                // Nếu đang chọn JTAtoZ không cho chọn recNameAtoZ
                adjustButtonStateUI(button, cusBindingFilter.JTAtoZ, cusBindingFilter.recAtoZ)
            }
        }

        // Recruiter btn list
        recNameButtons.forEach { button ->
            button.setOnClickListener {
                handleButtonSelection(recNameButtons, button)
                // Nếu đang chọn recNameAtoZ không cho chọn JTAtoZ
                adjustButtonStateUI(button, cusBindingFilter.recAtoZ, cusBindingFilter.JTAtoZ)
            }
        }

        // Posttime btn list
        postedTimeButtons.forEach { button ->
            button.setOnClickListener {
                handleButtonSelection(postedTimeButtons, button)
            }
        }

        // Workshift btn list
        workShiftButtons.forEach { button ->
            button.setOnClickListener {
                handleButtonSelection(workShiftButtons, button)
            }
        }

        // Seekbar slider
        cusBindingFilter.rangeslider.addOnSliderTouchListener(object : RangeSlider.OnSliderTouchListener {
            // Responds to when slider's touch event is being started
            override fun onStartTrackingTouch(slider: RangeSlider) {
            }
            // Responds to when slider's touch event is being stopped
            override fun onStopTrackingTouch(slider: RangeSlider) {
            }
        })
        // Responds to when slider's value is changed
        cusBindingFilter.rangeslider.addOnChangeListener { rangeSlider, value, fromUser ->
        }
        // Format định dạng hiển thị của tiền việt trên label
        cusBindingFilter.rangeslider.setLabelFormatter { value: Float ->
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = Currency.getInstance("VND")
            format.format(value.toDouble())
        }


        // reset filter btn
        cusBindingFilter.resetBtn.setOnClickListener {
            defaultSelectionFilterUI()
        }

        // apply filter btn
        cusBindingFilter.applyBtn.setOnClickListener {
            Toast.makeText(this, "Áp dụng bộ lọc!", Toast.LENGTH_SHORT).show()
            binding.rootNewJob.closeDrawer(GravityCompat.END)
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


    private fun handleButtonSelection(buttonList: List<Button>, selectedButton: Button) {
        // Cập nhật background của Button được chọn
        selectedButton.setBackgroundResource(R.drawable.custom_filter_btn_selected)
        for (btn in buttonList) {
            if (btn != selectedButton) {
                btn.setBackgroundResource(R.drawable.custom_filter_btn_default)
            }
        }
    }

    private fun defaultSelectionFilterUI() {
        // Các btn muốn đặt lại UI về default
        val selectedButtons = listOf(cusBindingFilter.JTAll, cusBindingFilter.recAll, cusBindingFilter.PTNewest, cusBindingFilter.WSAll)
        // Giá trị mặc định của RangeSlider
        val defaultValues = resources.getStringArray(R.array.initial_slider_values).map { it.toFloat() }

        val allButtons = listOf(
            // Job title buttons
            cusBindingFilter.JTAll, cusBindingFilter.JTAtoZ,
            // Recruiter name buttons
            cusBindingFilter.recAll, cusBindingFilter.recAtoZ,
            // Posted time buttons
            cusBindingFilter.PTAnytime, cusBindingFilter.PTNewest, cusBindingFilter.PTThisMonth,
            // Work shift buttons
            cusBindingFilter.WSAll, cusBindingFilter.WSMorning, cusBindingFilter.WSAfternoon
        )

        for (btn in allButtons) {
            btn.setBackgroundResource(  // Nếu ngoài ccacsbtn mặc định thì đổi tất cả về background default
                if (btn in selectedButtons) R.drawable.custom_filter_btn_selected
                else R.drawable.custom_filter_btn_default
            )
            // Đặt alpha và isEnabled về mặc định
            btn.alpha = 1.0F
            btn.isEnabled = true
        }

        // Đặt lại giá trị của RangeSlider về mặc định
        cusBindingFilter.rangeslider.setValues(defaultValues)
    }

    // Hàm để điều chỉnh trạng thái của targetButton dựa trên trạng thái của selectedButton
    private fun adjustButtonStateUI(curSelectedButton: Button, buttonToCheck: Button, targetButton: Button) {
        val isButtonToCheckSelected = (curSelectedButton == buttonToCheck)
        targetButton.apply {
            alpha = if (isButtonToCheckSelected) 0.4F else 1.0F
            isEnabled = !isButtonToCheckSelected
        }
    }


}