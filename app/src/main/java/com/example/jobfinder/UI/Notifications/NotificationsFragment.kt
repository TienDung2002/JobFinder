package com.example.jobfinder.UI.Notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.jobfinder.Datas.Model.NotificationsRowModel
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentNotificationsBinding


class NotificationsFragment : Fragment() {
    private lateinit var binding: FragmentNotificationsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        val notificationList = mutableListOf(
            NotificationsRowModel("Thông báo 1", "Applications for Google companies have entered for company review", "10 phút trước"),
            NotificationsRowModel("Thông báo 2", "Applications for Google companies have entered for company review Applications for Google companies have entered for company review Applications for Google companies have entered for company review Applications for Google companies have entered for company review Applications for Google companies have entered for company review Applications for Google companies have entered for company review Applications for Google companies have entered for company review Applications for Google companies have entered for company review", "20 phút trước"),
            NotificationsRowModel("Thông báo 3", "Nội dung thông báo 3", "30 phút trước"),
            NotificationsRowModel("Thông báo 4", "Nội dung thông báo 4", "40 phút trước"),
            NotificationsRowModel("Thông báo 5", "Nội dung thông báo 5", "50 phút trước")
        )

        // Fragment thì truyền "requireContext()" thay vì "this" như activity
        val recyclerView = binding.recyclerNotifications
        val adapter = NotificationsAdapter(notificationList,  requireContext(), binding.noNoti)
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter.setOnItemClickListener(object : NotificationsAdapter.OnItemClickListener {
            override fun onItemClick(view: View, position: Int, item: NotificationsRowModel) {
                if (view.id == R.id.txtDelete) {
                    adapter.removeItem(position)
                }
            }
        })






        // không được xóa
        return binding.root
    }


}