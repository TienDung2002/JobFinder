package com.example.jobfinder.UI.UsersProfile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jobfinder.R
import com.example.jobfinder.UI.Register.SeekerRegisterActivity
import com.example.jobfinder.Utils.PreventDoubleClick
import com.example.jobfinder.databinding.ActivityUserDetailBinding
import com.example.jobfinder.databinding.FragmentUserEditProfileBinding
import com.google.firebase.auth.FirebaseAuth

class UserDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDetailBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // account detail

        val userType = intent.getStringExtra("user_type")
        binding.profileAccount.setOnClickListener {
            if (PreventDoubleClick.checkClick()){
                if(userType == "NUser"){
                    val intent = Intent(this, UserEditProfile::class.java)
                    startfr(intent)
                } else if (userType == "BUser"){
                    val intent = Intent(this, RecruiterEditProfile::class.java)
                    startActivity(intent)
                }
            }
        }

    }

}