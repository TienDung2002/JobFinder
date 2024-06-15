package com.example.jobfinder.UI.FindNewJobs

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.Utils.GetData
import com.google.firebase.database.*
import java.text.Collator
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class FindNewJobViewModel : ViewModel() {
    private val OriginJobsList: MutableList<JobModel> = mutableListOf()
    private val _jobsListLiveData = MutableLiveData<List<JobModel>>()
    private val _sortedJobsLiveData = MutableLiveData<List<JobModel>>()
    private val _bookmarkStatus = MutableLiveData<Map<String, Boolean>>()
    private val database = FirebaseDatabase.getInstance().getReference("Job")
    var _isLoading = MutableLiveData<Boolean>()


    val jobsListLiveData: LiveData<List<JobModel>> get() = _jobsListLiveData
    val sortedJobsLiveData: LiveData<List<JobModel>> get() = _sortedJobsLiveData
    val bookmarkStatus: LiveData<Map<String, Boolean>> get() = _bookmarkStatus


    fun getJobsList(): List<JobModel> {
        return OriginJobsList
    }
    fun addJobsToJobsList(JobsData: JobModel) {
        OriginJobsList.add(JobsData)
        _jobsListLiveData.value = OriginJobsList
    }

    fun clearJobsList() {
        OriginJobsList.clear()
        _jobsListLiveData.value = OriginJobsList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sortFilter(ftJobTitle: Int, ftRecTitle: Int, ftPostTime: Int, minSalary:Float, maxSalary:Float, startHr:Int, endHr:Int) {
        val copyList = OriginJobsList.toMutableList()

        val collator = Collator.getInstance(Locale("vi", "VN"))

        val startHrString = if(startHr == 24){
            "23:59"
        }else{
            GetData.formatIntToTime(startHr)
        }

        val endHrString = if(endHr == 24){
            "23:59"
        }else{
            GetData.formatIntToTime(endHr)
        }

        // Chuyển đổi các chuỗi thời gian thành LocalTime
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val startHrTime = LocalTime.parse(startHrString, formatter)
        val endHrTime = LocalTime.parse(endHrString, formatter)

        var sortedList = when {
            // a-z job title
            ftJobTitle == 1 -> copyList.sortedWith(compareBy(collator) { it.jobTitle ?: "default jobTitle" })
            // a-z BUserName
            ftRecTitle == 1 -> copyList.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.BUserName ?: "default BUserName" })
            // new to old
            ftPostTime == 1 -> copyList.sortedByDescending { GetData.convertStringToDate(it.postDate.toString()) }
            else -> copyList
        }


        //sort theo tháng
        if (ftPostTime == 2) {
            val currentMonth = LocalDate.now().monthValue
            sortedList = sortedList.filter { job ->
                val jobMonth = job.postDate.toString().split("/")[1].toIntOrNull() ?: -1
                jobMonth == currentMonth
            }
        }

        // sort theo lương
        sortedList = sortedList.filter { job ->
            val salary = job.salaryPerEmp.toString().toFloatOrNull()
            salary != null && salary in minSalary..maxSalary
        }

        //sort theo gio lam
        sortedList = sortedList.filter { job ->
            val startTime = LocalTime.parse(job.startHr, formatter)
            val endTime = LocalTime.parse(job.endHr, formatter)
            startTime in startHrTime..endHrTime && endTime in startHrTime..endHrTime

        }

        _sortedJobsLiveData.value = sortedList
    }

    fun updateStatusToFirebase(userId :String,jobList: List<JobModel>) {
        val updatesMap = mutableMapOf<String, Any?>()
        for (jobModel in jobList) {
            updatesMap["/${jobModel.jobId}/buserName"] = jobModel.BUserName
            updatesMap["/${jobModel.jobId}/status"] = jobModel.status
        }
        database.child(userId).updateChildren(updatesMap)
            .addOnSuccessListener {
                // Tất cả các trạng thái đã được cập nhật thành công
            }
            .addOnFailureListener {
                // Xử lý lỗi
            }
    }




    // Cập nhật trạng thái bookmark
    fun updateBookmarkStatus(jobId: String, isBookmarked: Boolean) {
        val newStatus = _bookmarkStatus.value?.toMutableMap() ?: mutableMapOf()
        newStatus[jobId] = isBookmarked
        _bookmarkStatus.value = newStatus
    }
    fun getBookmarkStatus(jobId: String): Boolean {
        return _bookmarkStatus.value?.get(jobId) ?: false
    }

    fun updateJob(jobId: String, buserId: String, update: HashMap<String, Any>) {
        database.child(buserId).child(jobId).updateChildren(update)
    }

}

