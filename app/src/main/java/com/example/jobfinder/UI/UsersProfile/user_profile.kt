package com.example.jobfinder.UI.UsersProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jobfinder.Datas.Model.idAndRole
import com.example.jobfinder.Utils.FragmentHelper
import com.example.jobfinder.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class user_profile : Fragment() {
    private lateinit var binding:FragmentUserProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userRole: String



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater,container,false)

        userRole = getUserRole()
        binding.profileAccount.setOnClickListener {
            if (userRole=="NUser"){
                FragmentHelper.replaceFragment(childFragmentManager , binding.profileAccount, UserEditProfile())

            }else if (userRole=="BUser"){
                FragmentHelper.replaceFragment(childFragmentManager , binding.profileAccount, RecruiterEditProfile())

            }
        }
        return binding.root

    }

    private fun getUserRole(): String{
        val uid = auth.currentUser?.uid
        var result = "null"
        FirebaseDatabase.getInstance().getReference("UserRole").child(uid.toString()).get().addOnSuccessListener {
            val data: idAndRole? = it.getValue(idAndRole::class.java)
            if (data != null) {
                result = data.role.toString()
            }
        }
        return result

    }


}