package com.example.jobfinder.UI.UserDetailInfo

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
import com.example.jobfinder.databinding.ActivityBuserDetailInfoBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class BUserDetailInfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuserDetailInfoBinding
    lateinit var viewModel: ProfileViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuserDetailInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        val database = FirebaseDatabase.getInstance().reference

        val userId = intent.getStringExtra("uid")
        val from = intent.getStringExtra("fromAct")

        setUpButn(userId.toString())

        if (from!= null){
            binding.recuitterInfoBtnHolder.visibility = View.VISIBLE
        }
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
                    Log.e("RecruterEditProfileFragment", "Database error: ${error.message}")
                }
            })
            database.child("BUserInfo").child(it).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val description = snapshot.child("description").getValue(String::class.java)
                    description?.let {
                        viewModel.des = it
                        if(it == ""){
                            binding.editProfileDescription.setText(R.string.no_job_des2)
                        }else {
                            binding.editProfileDescription.setText(viewModel.des)
                        }
                    }
                    val busType = snapshot.child("business_type").getValue(String::class.java)
                    busType?.let {
                        viewModel.busType = it
                        if(it == ""){
                            binding.editProfileBustype.setText(R.string.error_invalid_BusSec)
                        }else {
                            binding.editProfileBustype.setText(viewModel.busType)
                        }
                    }
                    val busSec = snapshot.child("business_sector").getValue(String::class.java)
                    busSec?.let {
                        viewModel.busSec = it
                        if(it == ""){
                            binding.editProfileBusSec.text = getString(R.string.blank_sector)
                        }else {
                            binding.editProfileBusSec.text = viewModel.busSec
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("RecruterEditProfileFragment", "Database error: ${error.message}")
                }
            })

            viewModel.userid = it
            binding.animationView.visibility = View.GONE

        }

        binding.backButton.setOnClickListener {
            val resultIntent = Intent()
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        RetriveImg.retrieveImage(viewModel.userid, binding.profileImage)
    }

    fun setUpButn(uid:String){
        binding.addCashBtn.setOnClickListener {

        }

        binding.disableBtn.setOnClickListener {
        }
    }
}