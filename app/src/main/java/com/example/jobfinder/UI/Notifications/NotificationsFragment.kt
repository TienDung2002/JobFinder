package com.example.jobfinder.UI.Notifications

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.NotificationsRowModel
import com.example.jobfinder.R
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.FragmentNotificationsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class NotificationsFragment : Fragment() {
    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
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

                val recyclerView = binding.recyclerNotifications
                val adapter =
                    NotificationsAdapter(notificationList, requireContext(), binding.noNoti)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())

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
    }
}