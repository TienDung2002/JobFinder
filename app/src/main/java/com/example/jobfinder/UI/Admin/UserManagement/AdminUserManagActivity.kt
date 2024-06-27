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

        val adapter = AdminUserManagementAdapter(mutableListOf())
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
                    if (userInfo.userRole == "NUser"){
                        val intent = Intent(binding.root.context, AdminUMNUserDetail::class.java)
                        intent.putExtra("uid", userInfo.userBasicInfo.userId)
                        startActivityForResult(intent, REQUEST_CODE)
                        isActivityOpened = true
                    }
                    if (userInfo.userRole == "BUser"){
                        val intent = Intent(this@AdminUserManagActivity, BUserDetailInfoActivity::class.java)
                        intent.putExtra("uid", userInfo.userBasicInfo.userId)
                        startActivityForResult(intent, REQUEST_CODE)
                        isActivityOpened = true
                    }
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