package com.example.jobfinder.UI.ReviewUser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.ReviewDataModel

class WNuserReviewedViewModel: ViewModel()  {
    private val _reviewsList = MutableLiveData<List<ReviewDataModel>>()
    private val _isFetchingData = MutableLiveData<Boolean>()
    val reviewsList: LiveData<List<ReviewDataModel>> get() = _reviewsList
    val isFetchingData: LiveData<Boolean> get() = _isFetchingData


    fun fetchReviews() {
        // Giả lập fetch dữ liệu
        _isFetchingData.value = true

        val fetchedReviews = listOf(
            ReviewDataModel("User1", "4.5", "Developer", "Great job!"),
            ReviewDataModel("User2", "4.0", "Designer", "Good work."),
            ReviewDataModel("User1", "4.5", "Developer", "Great job!"),
            ReviewDataModel("User2", "4.0", "Designer", "Good work."),
            ReviewDataModel("User1", "4.5", "Developer", "Great job!"),
            ReviewDataModel("User2", "4.0", "Designer", "Good work.")
        )
        _reviewsList.postValue(fetchedReviews)
        _isFetchingData.value = false

    }
}