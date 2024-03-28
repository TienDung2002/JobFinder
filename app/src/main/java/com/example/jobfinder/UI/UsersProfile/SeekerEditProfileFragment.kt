package com.example.jobfinder.UI.UsersProfile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil.setContentView
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentSeekerEditProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SeekerEditProfileFragment : Fragment() {
    private lateinit var binding: FragmentSeekerEditProfileBinding
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()


        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid
        userId?.let {
            database.child("UserBasicInfo").child(it).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userName = snapshot.child("name").getValue(String::class.java)
                    userName?.let {
                        binding.editProfileName.setText(it)
                    }
                    val email = snapshot.child("email").getValue(String::class.java)
                    email?.let {
                        binding.editProfileEmail.setText(it)
                    }
                    val phone = snapshot.child("phone_num").getValue(String::class.java)
                    phone?.let {
                        binding.editProfilePhonenum.setText(it)
                    }
                    val address = snapshot.child("address").getValue(String::class.java)
                    address?.let {
                        binding.editProfileAddress.setText(it)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("UserProfileMenuFragment", "Database error: ${error.message}")
                }
            })
            database.child("NUserInfo").child(it).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val age = snapshot.child("age").getValue(Int::class.java)
                    age?.let {
                        binding.editProfileAge.setText(it.toString())
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("UserProfileMenuFragment", "Database error: ${error.message}")
                }
            })
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeekerEditProfileBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = binding.editProfileName
        val email = binding.editProfileEmail
        val address = binding.editProfileAddress
        val phone = binding.editProfilePhonenum
        val age = binding.editProfileAge
        val save = binding.editProfileSaveChange


        name.isEnabled = false
        address.isEnabled = false
        phone.isEnabled = false
        age.isEnabled = false

        //button sửa
        binding.editProfileEditbtn.setOnClickListener {
            name.isEnabled = true
            address.isEnabled = true
            phone.isEnabled = true
            age.isEnabled = true

            save.visibility = View.VISIBLE
        }
        //button save
        save.setOnClickListener {
            val newName = name.text.toString()
            val newAddress = address.text.toString()
            val newPhone = phone.text.toString()
            val newAge = age.text.toString().toIntOrNull()

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            userId?.let {
                val userBI = FirebaseDatabase.getInstance().reference.child("UserBasicInfo").child(it)
                val NUser = FirebaseDatabase.getInstance().reference.child("NUserInfo").child(it)
                userBI.child("name").setValue(newName)
                userBI.child("address").setValue(newAddress)
                userBI.child("phone_num").setValue(newPhone)
                NUser.child("age").setValue(newAge)
            }
            name.isEnabled = false
            address.isEnabled = false
            phone.isEnabled = false
            age.isEnabled = false

            save.visibility = View.GONE

        }
        //button back
        binding.editProfileBackbtn.setOnClickListener {
            val intent = Intent(requireContext(), UserDetailActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

}