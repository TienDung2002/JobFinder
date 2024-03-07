package com.example.jobfinder.UI.Notifications

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.NotificationsRowModel
import com.example.jobfinder.R

class NotificationsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        val notificationList = mutableListOf(
            NotificationsRowModel("Thông báo 1", "Applications for Google companies have entered for company review", "10 phút trước"),
            NotificationsRowModel("Thông báo 2", "Applications for Google companies have entered for company review Applications for Google companies have entered for company review Applications for Google companies have entered for company review Applications for Google companies have entered for company review Applications for Google companies have entered for company review Applications for Google companies have entered for company review Applications for Google companies have entered for company review Applications for Google companies have entered for company review", "20 phút trước"),
            NotificationsRowModel("Thông báo 3", "Nội dung thông báo 3", "30 phút trước"),
            NotificationsRowModel("Thông báo 4", "Nội dung thông báo 4", "40 phút trước"),
            NotificationsRowModel("Thông báo 5", "Nội dung thông báo 5", "50 phút trước")
        )

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerNotifications)
        val noNotiLayout: ConstraintLayout = findViewById(R.id.no_noti)
        val adapter = NotificationsAdapter(notificationList, noNotiLayout)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter.setOnItemClickListener(object : NotificationsAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, item: NotificationsRowModel) {
                if (view.id == R.id.txtDelete) {
                    adapter.removeItem(position)
                }
            }
        })
    }
}
