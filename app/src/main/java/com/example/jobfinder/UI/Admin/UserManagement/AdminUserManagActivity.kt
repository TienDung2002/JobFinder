package com.example.jobfinder.UI.Admin.UserManagement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.jobfinder.Datas.Model.AdminModel.BasicInfoAndRole
import com.example.jobfinder.UI.UserDetailInfo.BUserDetailInfoActivity
import com.example.jobfinder.databinding.ActivityAdminUserManagBinding


class AdminUserManagActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdminUserManagBinding
    private val viewModel: AdminUserManagementViewModel by viewModels()
    lateinit var adapter: AdminUserManagementAdapter
    private var isActivityOpened = false
    private val REQUEST_CODE = 1002

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUserManagBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // nút back về
        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        adapter = AdminUserManagementAdapter(mutableListOf())
        binding.recyclerUserList.adapter = adapter
        binding.recyclerUserList.layoutManager = LinearLayoutManager(this)

        viewModel.fetchUserList()

        viewModel.userList.observe(this){userList->
            adapter.updateData(userList)
            checkEmptyAdapter(userList)
        }

        adapter.setOnItemClickListener(object : AdminUserManagementAdapter.OnItemClickListener {
            override fun onItemClick(userInfo: BasicInfoAndRole) {
                if (!isActivityOpened) {
                    val intent = when (userInfo.userRole) {
                        "NUser" -> Intent(this@AdminUserManagActivity, AdminUMNUserDetail::class.java)
                        "BUser" -> Intent(this@AdminUserManagActivity, BUserDetailInfoActivity::class.java)
                        else -> null // Handle other roles if needed
                    }

                    if (intent != null) {
                        intent.putExtra("uid", userInfo.userBasicInfo.user_id.toString())
                        intent.putExtra("accStatus", userInfo.accountStatus.toString())
                        startActivityForResult(intent, REQUEST_CODE)
                    }
                    isActivityOpened = true
                }
            }
        })


    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            isActivityOpened = false
            viewModel.fetchUserList()
        }
    }

    private fun checkEmptyAdapter(list: MutableList<BasicInfoAndRole>) {
        if (list.isEmpty()) {
            binding.noUser.visibility = View.VISIBLE
            binding.animationView.visibility = View.GONE
        } else {
            binding.noUser.visibility = View.GONE
            binding.animationView.visibility = View.GONE
        }
    }

}