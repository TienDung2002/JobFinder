package com.example.jobfinder.UI.UsersProfile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.DataBindingUtil.setContentView
import com.example.jobfinder.R
import com.example.jobfinder.Utils.VerifyField
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
                    val age = snapshot.child("age").getValue(String::class.java)
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
            val newAge = age.text.toString()

            val isValidName = newName.isNotEmpty()
            val isValidAddress = newAddress.isNotEmpty()
            val isValidPhone = VerifyField.isValidPhoneNumber(newPhone)
            val isValidAge = VerifyField.isValidAge(newAge)

            name.error = if (isValidName) null else getString(R.string.error_invalid_name)
            address.error = if (isValidAddress) null else getString(R.string.error_invalid_addr)
            phone.error = if (isValidPhone) null else getString(R.string.error_invalid_hotline)
            age.error = if (isValidAge) null else getString(R.string.error_invalid_Age)

            if (isValidName && isValidAddress && isValidPhone && isValidAge){
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
            } else {
                checkToAutoFocus(isValidName , isValidAddress , isValidPhone , isValidAge)
            }



        }
        //button back
        binding.editProfileBackbtn.setOnClickListener {
            val resultIntent = Intent()
            requireActivity().setResult(Activity.RESULT_OK, resultIntent)
            requireActivity().finish()
        }
    }
    private fun checkToAutoFocus(vararg isValidFields: Boolean) {
        val invalidFields = mutableListOf<EditText>()
        for ((index, isValid) in isValidFields.withIndex()) {
            if (!isValid) {
                when (index) {
                    0 -> invalidFields.add(binding.editProfileName)
                    1 -> invalidFields.add(binding.editProfilePhonenum)
                    2 -> invalidFields.add(binding.editProfileAddress)
                    3 -> invalidFields.add(binding.editProfileAge)

                }
            }
        }

        if (invalidFields.isNotEmpty()) {
            invalidFields.first().requestFocus()
        }
    }

}