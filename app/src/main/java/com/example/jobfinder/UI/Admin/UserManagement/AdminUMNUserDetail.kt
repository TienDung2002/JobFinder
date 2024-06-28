package com.example.jobfinder.UI.Admin.UserManagement

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.jobfinder.R
import com.example.jobfinder.UI.UsersProfile.ProfileViewModel
import com.example.jobfinder.Utils.RetriveImg
import com.example.jobfinder.databinding.ActivityNuserDetailInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminUMNUserDetail : AppCompatActivity() {
    private lateinit var binding: ActivityNuserDetailInfoBinding

    lateinit var viewModel: ProfileViewModel

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNuserDetailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = FirebaseDatabase.getInstance().reference
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val userId = intent.getStringExtra("uid")
        binding.desHolder.visibility = View.GONE
        binding.recyclerHolder.visibility = View.GONE
        binding.rejectBtn.text = getString(R.string.add_cash_to_wallet_btn)
        binding.approveBtn.text = getString(R.string.disable_acc)

        binding.animationView.visibility = View.VISIBLE

        if (userId != null) {
            setupUserInformation(database, userId)
            setupButtons()
        }

    }

    override fun onResume() {
        super.onResume()
        RetriveImg.retrieveImage(viewModel.userid, binding.profileImage)
    }

    private fun setupUserInformation(database: DatabaseReference, userId: String) {
        database.child("UserBasicInfo").child(userId).addListenerForSingleValueEvent(object :
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

                binding.animationView.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SeekerEditProfileFragment", "Database error: ${error.message}")
            }
        })

        database.child("NUserInfo").child(userId).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val age = snapshot.child("age").getValue(String::class.java)
                age?.let {
                    viewModel.age = it
                    if (it == "") {
                        binding.editProfileAge.setText(R.string.blank_age)
                    } else {
                        binding.editProfileAge.setText(viewModel.age)
                    }
                }
                val gender = snapshot.child("gender").getValue(String::class.java)
                gender?.let {
                    viewModel.gender = it
                    if (it == "") {
                        binding.editProfileGender.setText(R.string.error_invalid_Gender)
                    } else {
                        binding.editProfileGender.setText(viewModel.gender)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("SeekerEditProfileFragment", "Database error: ${error.message}")
            }
        })

        viewModel.userid = userId
    }

    private fun setupButtons() {

        binding.backButton.setOnClickListener {
            sendResultAndFinish()
        }
        // disable
        binding.approveBtn.setOnClickListener {

        }
        //add cash
        binding.rejectBtn.setOnClickListener {

        }
    }

    private fun sendResultAndFinish() {
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

}