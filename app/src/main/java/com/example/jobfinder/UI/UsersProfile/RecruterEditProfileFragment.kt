package com.example.jobfinder.UI.UsersProfile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import com.example.jobfinder.R
import com.example.jobfinder.Utils.VerifyField
import com.example.jobfinder.databinding.FragmentRecruterEditProfileBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class RecruterEditProfileFragment : Fragment() {
    private lateinit var binding: FragmentRecruterEditProfileBinding
    private lateinit var auth: FirebaseAuth
    private var BusTypeChoosed = false
    private var BusSecChoosed = false


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
            database.child("BUserInfo").child(it).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val tax = snapshot.child("tax_code").getValue(String::class.java)
                    tax?.let {
                        binding.editProfileTaxnum.setText(it)
                    }
                    val description = snapshot.child("description").getValue(String::class.java)
                    description?.let {
                        binding.editProfileDescription.setText(it)
                    }
                    val busType = snapshot.child("business_type").getValue(String::class.java)
                    busType?.let {
                        binding.editProfileBustype.setText(it)
                    }
                    val busSec = snapshot.child("business_sector").getValue(String::class.java)
                    busSec?.let {
                        binding.editProfileBusSec.setText(it)
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
    ): View? {
        binding = FragmentRecruterEditProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val name = binding.editProfileName
        val email = binding.editProfileEmail
        val address = binding.editProfileAddress
        val phone = binding.editProfilePhonenum
        val description = binding.editProfileDescription
        val save = binding.editProfileSave
        val busType = binding.editProfileBustype
        val busSec = binding.editProfileBusSec
        val tax = binding.editProfileTaxnum

        name.isEnabled = false
        address.isEnabled = false
        phone.isEnabled = false
        description.isEnabled = false
        busType.isEnabled = false
        busSec.isEnabled = false
        tax.isEnabled = false


        //button sửa
        binding.editProfileEditbtn.setOnClickListener {
            name.isEnabled = true
            address.isEnabled = true
            phone.isEnabled = true
            description.isEnabled = true
            busType.isEnabled = true
            busSec.isEnabled = true
            tax.isEnabled = true

            save.visibility = View.VISIBLE
        }

        save.setOnClickListener {
            val newName = name.text.toString()
            val newAddress = address.text.toString()
            val newPhone = phone.text.toString()
            val newDes = description.text.toString()
            val newTax = tax.text.toString()
            val newBusType = busType.text.toString()
            val newBusSec = busSec.text.toString()

            val isValidName = newName.isNotEmpty()
            val isValidAddress = newAddress.isNotEmpty()
            val isValidPhone = VerifyField.isValidPhoneNumber(newPhone)
            val isValidDes = newDes.isNotEmpty()
            val isValidTax = newTax.isNotEmpty()
            val isValidBusType = newBusType.isNotEmpty()
            val isValidBusSec = newBusSec.isNotEmpty()


            name.error = if (isValidName) null else getString(R.string.error_invalid_name)
            address.error = if (isValidAddress) null else getString(R.string.error_invalid_addr)
            phone.error = if (isValidPhone) null else getString(R.string.error_invalid_hotline)
            description.error = if (isValidDes) null else getString(R.string.error_invalid_des)
            busType.error = if (isValidBusType) null else getString(R.string.error_invalid_BusType)
            busSec.error = if (isValidBusSec) null else getString(R.string.error_invalid_BusSec)
            tax.error = if (isValidTax) null else getString(R.string.error_invalid_tax)

            if (isValidName && isValidAddress && isValidPhone && isValidDes && isValidBusType && isValidBusSec && isValidTax) {
                val userId = FirebaseAuth.getInstance().currentUser?.uid
                userId?.let {
                    val userBI = FirebaseDatabase.getInstance().reference.child("UserBasicInfo").child(it)
                    val Buser = FirebaseDatabase.getInstance().reference.child("BUserInfo").child(it)
                    userBI.child("name").setValue(newName)
                    userBI.child("address").setValue(newAddress)
                    userBI.child("phone_num").setValue(newPhone)
                    Buser.child("description").setValue(newDes)
                    Buser.child("tax_code").setValue(newTax)
                    Buser.child("business_sector").setValue(newBusSec)
                    Buser.child("business_type").setValue(newBusType)
                }

                name.isEnabled = false
                address.isEnabled = false
                phone.isEnabled = false
                description.isEnabled = false
                busType.isEnabled = false
                busSec.isEnabled = false
                tax.isEnabled = false

                save.visibility = View.GONE
            } else {
                checkToAutoFocus(isValidName , isValidAddress , isValidPhone , isValidDes , isValidBusType , isValidBusSec , isValidTax)
            }





        }

        //button back
        binding.editProfileBackbtn.setOnClickListener {
            val intent = Intent(requireContext(), UserDetailActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        busType.setOnClickListener {
            BusTypeSelect(it)
            BusTypeChoosed = true
        }

        busSec.setOnClickListener {
            BusSecSelect(it)
            BusSecChoosed = true
        }



    }

    private fun showPopupMenu(view: View, menuResId: Int, itemClickListener: (String) -> Unit) {
        val popupMenu = PopupMenu(context, view)
        popupMenu.menuInflater.inflate(menuResId, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            val itemTitle = menuItem.title.toString()
            itemClickListener.invoke(itemTitle)
            true
        }
        popupMenu.show()
    }

    private fun BusTypeSelect(view: View){
        showPopupMenu(view, R.menu.business_type_menu) { selectedBusType ->
            binding.editProfileBustype.text = selectedBusType
        }
    }
    private fun BusSecSelect(view: View){
        showPopupMenu(view, R.menu.business_sector_menu) { selectedBusSec ->
            binding.editProfileBusSec.text = selectedBusSec
        }
    }

    private fun checkToAutoFocus(vararg isValidFields: Boolean) {
        val invalidFields = mutableListOf<EditText>()
        val invalidField = mutableListOf<TextView>()
        for ((index, isValid) in isValidFields.withIndex()) {
            if (!isValid) {
                when (index) {
                    0 -> invalidFields.add(binding.editProfileName)
                    1 -> invalidFields.add(binding.editProfilePhonenum)
                    2 -> invalidFields.add(binding.editProfileAddress)
                    3 -> invalidFields.add(binding.editProfileTaxnum)
                    4 -> invalidFields.add(binding.editProfileDescription)
                    5 -> invalidField.add(binding.editProfileBustype)
                    6 -> invalidField.add(binding.editProfileBusSec)
                }
            }
        }

        if (invalidFields.isNotEmpty()) {
            invalidFields.first().requestFocus()
        }
    }
}