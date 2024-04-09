package com.example.jobfinder.UI.UsersProfile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.jobfinder.Utils.FragmentHelper
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.ActivityAccountBinding
import com.google.firebase.auth.FirebaseAuth

class AccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAccountBinding
    private lateinit var auth: FirebaseAuth
    private var userRole: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //firebase
        auth = FirebaseAuth.getInstance()

        // gọi hàm getUserRole từ trong Utils.GetData
        GetData.getUserRole { role ->
            role?.let {
                // Gán giá trị chỉ khi role không null (logic là thế nhưng hàm getUserRole t cho trả về "null string" thay vì null)
                userRole = it
                if (userRole=="NUser"){
                    FragmentHelper.replaceFragment(supportFragmentManager , binding.profileframelayout, SeekerEditProfileFragment())
                    binding.animationView.visibility = View.GONE
                }else if (userRole=="BUser"){
                    FragmentHelper.replaceFragment(supportFragmentManager , binding.profileframelayout, RecruterEditProfileFragment())
                    binding.animationView.visibility = View.GONE
                }
            }
        }


    }
}