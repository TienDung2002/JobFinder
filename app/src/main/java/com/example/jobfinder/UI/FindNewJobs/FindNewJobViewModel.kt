package com.example.jobfinder.UI.FindNewJobs

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.JobModel
import com.example.jobfinder.Utils.GetData
import com.google.firebase.database.*
import java.text.Collator
import java.time.LocalDate
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


    // Coppy list origin sang list khác và sắp xếp để tạo list có các việc theo ngày đăng mới nhất
//    val sortedOriJobsListByPostDate = OriginJobsList.toMutableList().sortedByDescending { it.postDate }
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
    fun sortFilter(ftJobTitle: Int, ftRecTitle: Int, ftPostTime: Int) {
        val copyList = OriginJobsList.toMutableList()

        val collator = Collator.getInstance(Locale("vi", "VN"))

        var sortedList = when {
            ftJobTitle == 1 -> copyList.sortedWith(compareBy(collator) { it.jobTitle ?: "default jobTitle" })
            ftRecTitle == 1 -> copyList.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.BUserName ?: "default BUserName" })
            ftPostTime == 1 -> copyList.sortedByDescending { GetData.convertStringToDate(it.postDate.toString()) }
            else -> copyList
        }

        if (ftPostTime == 2) {
            val currentMonth = LocalDate.now().monthValue
            sortedList = sortedList.filter { job ->
                val jobMonth = job.postDate.toString().split("/")[1].toIntOrNull() ?: -1
                jobMonth == currentMonth
            }
        }

        _sortedJobsLiveData.value = sortedList
    }

    // Các hàm Sort
    fun resetOriginAdapData(){
        _sortedJobsLiveData.value = OriginJobsList
    }
    fun sortByJobTitle() {
        val copyList = OriginJobsList.toMutableList()
        val collator = Collator.getInstance(Locale("vi", "VN"))
        val sortedList = copyList.sortedWith(compareBy(collator) { it.jobTitle ?: "default jobTitle" })
        _sortedJobsLiveData.value = sortedList
    }

    fun sortByBuserName() {
        val copyList = OriginJobsList.toMutableList()
        val sortedList = copyList.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER) { it.BUserName ?: "default BUserName" })
        _jobsListLiveData.value = sortedList
    }

    fun sortByPostDate(){
        var copyList = OriginJobsList.toMutableList().toList()
        copyList = copyList.sortedByDescending { GetData.convertStringToDate(it.postDate.toString()) }
        _sortedJobsLiveData.value = copyList
    }
    fun sortByPTMonth(){}
    fun sortByWorkShift(){}
    fun sortBySalaryRange(){}






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

}

