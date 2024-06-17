package com.example.jobfinder.UI.UserDetailInfo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.JobHistoryModel
import com.example.jobfinder.Datas.Model.ReviewDataModel

class WNuserReviewedViewModel: ViewModel()  {
    private val _reviewsList = MutableLiveData<List<JobHistoryModel>>()
    private val _isFetchingData = MutableLiveData<Boolean>()
    val reviewsList: LiveData<List<JobHistoryModel>> get() = _reviewsList
    val isFetchingData: LiveData<Boolean> get() = _isFetchingData


    fun fetchReviews() {
        // Giả lập fetch dữ liệu
        _isFetchingData.value = true

        val fetchedReviews = listOf(
            JobHistoryModel("1", "Rửa bát lau sàn sàn sàn", "17/06/2024", "abc", "1", "4.5", "Rất tốt, cực kì tốt vip pro mã", "1", "Nguyễn Văn B"),
            JobHistoryModel("1", "Rửa bát lau sàn2", "17/06/2024", "abc", "2", "4.5", "Rất tốt, cực kì tốt vip pro mã Rất tốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốttốt", "1", "Nguyễn Văn B"),
            JobHistoryModel("1", "Rửa bát lau sàn3", "17/06/2024", "abc", "3", "4.5", "Rất tốt, cực kì tốt vip pro mã", "1", "Nguyễn Văn B"),
            JobHistoryModel("1", "Rửa bát lau sàn4", "17/06/2024", "abc", "4", "4.5", "Rất tốt, cực kì tốt vip pro mã", "1", "Nguyễn Văn B"),
            JobHistoryModel("1", "Rửa bát lau sàn5", "17/06/2024", "abc", "5", "4.5", "Rất tốt, cực kì tốt vip pro mã", "1", "Nguyễn Văn B"),
        )
        _reviewsList.postValue(fetchedReviews)
        _isFetchingData.value = false

    }
}