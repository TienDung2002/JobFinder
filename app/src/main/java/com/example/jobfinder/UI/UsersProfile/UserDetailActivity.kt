package com.example.jobfinder.UI.UsersProfile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobfinder.Utils.FragmentHelper
import com.example.jobfinder.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)



        // add fragment mặc định khi mới mở
        FragmentHelper.replaceFragment(supportFragmentManager, binding.profileframelayout, user_profile())

    }



}