package com.example.jobfinder.UI.UserDetailInfo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.jobfinder.Datas.Model.ApplicantsModel
import com.example.jobfinder.UI.UsersProfile.ProfileViewModel
import com.example.jobfinder.Utils.RetriveImg
import com.example.jobfinder.databinding.ActivityNuserDetailInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class NUserDetailInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNuserDetailInfoBinding
    lateinit var viewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuserDetailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.getInstance().reference
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val applicant = intent.getParcelableExtra<ApplicantsModel>("nuser_applicant")

        if (applicant != null) {

            val userId = applicant.userId.toString()
            userId?.let {
                database.child("UserBasicInfo").child(it).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val userName = snapshot.child("name").getValue(String::class.java)
                        userName?.let {
                            viewModel.name = it
                            binding.editProfileName.setText(viewModel.name)
                        }
                        val email = snapshot.child("email").getValue(String::class.java)
                        email?.let {
                            viewModel.email = it
                            binding.editProfileEmail.setText(viewModel.email)
                        }
                        val phone = snapshot.child("phone_num").getValue(String::class.java)
                        phone?.let {
                            viewModel.phone = it
                            binding.editProfilePhonenum.setText(viewModel.phone)
                        }
                        val address = snapshot.child("address").getValue(String::class.java)
                        address?.let {
                            viewModel.address = it
                            binding.editProfileAddress.setText(viewModel.address)
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("SeekerEditProfileFragment", "Database error: ${error.message}")
                    }
                })
                database.child("NUserInfo").child(it).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val age = snapshot.child("age").getValue(String::class.java)
                        age?.let {
                            viewModel.age = it
                            binding.editProfileAge.setText(viewModel.age)
                        }
                        val gender = snapshot.child("gender").getValue(String::class.java)
                        gender?.let {
                            viewModel.gender = it
                            binding.editProfileGender.setText(viewModel.gender)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("SeekerEditProfileFragment", "Database error: ${error.message}")
                    }
                })
                viewModel.userid = it

            }
        }

        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()

        RetriveImg.retrieveImage(viewModel.userid, binding.profileImage, this@NUserDetailInfoActivity, viewModel)
    }

}