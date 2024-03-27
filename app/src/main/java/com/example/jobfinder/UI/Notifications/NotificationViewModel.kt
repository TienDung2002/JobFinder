package com.example.jobfinder.UI.Notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.jobfinder.Datas.Model.NotificationsRowModel

class NotificationViewModel: ViewModel() {
    private val _notificationList = MutableLiveData<List<NotificationsRowModel>>()
    val notificationList: LiveData<List<NotificationsRowModel>>
        get() = _notificationList

    // Hàm cập nhật danh sách thông báo
    fun updateNotificationList(list: List<NotificationsRowModel>) {
        _notificationList.value = list
    }
}