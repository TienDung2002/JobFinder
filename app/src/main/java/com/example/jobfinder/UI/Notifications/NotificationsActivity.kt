package com.example.jobfinder.UI.Notifications

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.NotificationsRowModel
import com.example.jobfinder.R

class NotificationsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)
        val notificationList = listOf(
            NotificationsRowModel("Thông báo 1", "Applications for Google companies have entered for company review", "10 phút trước", "Xóa"),
            NotificationsRowModel("Thông báo 2", "Nội dung thông báo 2", "20 phút trước", "Xóa"),
            NotificationsRowModel("Thông báo 3", "Nội dung thông báo 3", "30 phút trước", "Xóa"),
            NotificationsRowModel("Thông báo 4", "Nội dung thông báo 4", "40 phút trước", "Xóa"),
            NotificationsRowModel("Thông báo 5", "Nội dung thông báo 5", "50 phút trước", "Xóa")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerNotifications)
        val adapter = NotificationsAdapter(notificationList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

    }
}
