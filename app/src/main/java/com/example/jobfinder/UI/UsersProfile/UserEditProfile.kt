package com.example.jobfinder.UI.UsersProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jobfinder.R
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.FragmentUserEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar


class UserEditProfile : Fragment() {
    private lateinit var binding: FragmentUserEditProfileBinding
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //firebase
        auth = FirebaseAuth.getInstance()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserEditProfileBinding.inflate(inflater, container, false)
        binding.editProfileBackbtn.setOnClickListener{
            val nameEdit = binding.editProfileName.text.toString()
            val emailEdit = binding.editProfileEmail.text.toString()
            val phoneEdit = binding.editProfilePhonenum.text.toString()
            val addressEdit = binding.editProfileAddress.text.toString()

            val isValidName = nameEdit.isNotEmpty()
            val isValidEmail = VerifyField.isValidEmail(emailEdit)
            val isValidPhone = VerifyField.isValidPhoneNumber(phoneEdit)
            val isValidAddress = addressEdit.isNotEmpty()

            binding.editProfileName.error = if (isValidName) null else getString(R.string.error_invalid_name)
            binding.editProfileEmail.error = if (isValidEmail) null else getString(R.string.error_invalid_email)
            binding.editProfilePhonenum.error = if (isValidPhone) null else getString(R.string.error_invalid_phone)
            binding.editProfileAddress.error = if (isValidAddress) null else getString(R.string.error_invalid_addr)

        }
        return binding.root
    }



}