package com.example.jobfinder.UI.Notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.NotificationsRowModel
import com.example.jobfinder.R
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class NotificationsFragment : Fragment() {
    private lateinit var binding: FragmentNotificationsBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var viewModel: NotificationViewModel
    val uid = auth.currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Khởi tạo viewmodel nếu fragment đã dc add vào activity
        if (isAdded) {
            viewModel = ViewModelProvider(this).get(NotificationViewModel::class.java)
        }

        binding.deleteAll.setOnClickListener{
            val uid = auth.currentUser?.uid

            FirebaseDatabase.getInstance()
                .getReference("Notifications")
                .child(uid.toString()).removeValue()
            fetchNotificationsFromFirebase()
        }

        // Quan sát dữ liệu trong ViewModel và cập nhật giao diện khi có thay đổi
        viewModel.notificationList.observe(viewLifecycleOwner) { list ->
            updateRecyclerView(list)
        }


        // Kiểm tra xem dữ liệu đã được lưu trong ViewModel chưa
        if (viewModel.notificationList.value == null) {
            // Nếu chưa có dữ liệu, thực hiện fetch từ Firebase
            fetchNotificationsFromFirebase()
        } else {
            // Nếu đã có dữ liệu, cập nhật RecyclerView trực tiếp
            updateRecyclerView(viewModel.notificationList.value!!)
        }
    }


    private fun updateRecyclerView(notificationList: List<NotificationsRowModel>) {
        val convertToMutableList = notificationList.toMutableList()
        // Cập nhật RecyclerView với danh sách thông báo mới
        val adapter = NotificationsAdapter(convertToMutableList, requireContext(), binding.noNoti)
        binding.recyclerNotifications.adapter = adapter
        binding.recyclerNotifications.layoutManager = LinearLayoutManager(requireContext())

        // Xác định nếu người dùng nhấn vào nút "Xóa"
        adapter.setOnItemClickListener(object : NotificationsAdapter.OnItemClickListener {
            override fun onItemClick(
                view: View,
                position: Int,
                item: NotificationsRowModel
            ) {
                if (view.id == R.id.txtDelete) {
                    adapter.removeItem(position)
                    FirebaseDatabase.getInstance()
                        .getReference("Notifications")
                        .child(uid.toString())
                        .child(item.notiId.toString()).removeValue()
                }
            }
        })
        binding.animationView.visibility = View.GONE
    }


    // Fetch data từ Firebase và cập nhật dữ liệu trong ViewModel
    private fun fetchNotificationsFromFirebase() {
        val uid = auth.currentUser?.uid

        FirebaseDatabase.getInstance()
            .getReference("Notifications")
            .child(uid.toString())
            .get()
            .addOnSuccessListener { dataSnapshot ->
                val notificationList = mutableListOf<NotificationsRowModel>()
                for (notiSnapshot in dataSnapshot.children) {
                    val notiId = notiSnapshot.child("notiId").getValue(String::class.java)
                    val from = notiSnapshot.child("from").getValue(String::class.java)
                    val detail = notiSnapshot.child("detail").getValue(String::class.java)
                    val date = notiSnapshot.child("date").getValue(String::class.java)

                    val notification = NotificationsRowModel(notiId, from, detail, date)
                    notificationList.add(notification)
                }
                notificationList.sortByDescending { GetData.convertStringToDate(it.date.toString()) }

                // Cập nhật danh sách thông báo trong ViewModel
                viewModel.updateNotificationList(notificationList)
            }
    }
}