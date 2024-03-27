package com.example.jobfinder.UI.UsersProfile

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobfinder.R
import com.example.jobfinder.Utils.FragmentHelper
import com.example.jobfinder.Utils.GetData
import com.example.jobfinder.databinding.ActivityAccountBinding
import com.example.jobfinder.databinding.ActivityWalletBinding
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

                }else if (userRole=="BUser"){
                    FragmentHelper.replaceFragment(supportFragmentManager , binding.profileframelayout, RecruterEditProfileFragment())

                }
            }
        }


    }
}