package com.example.jobfinder.UI.UsersProfile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jobfinder.R
import com.example.jobfinder.databinding.FragmentRecruterEditProfileBinding


class RecruterEditProfileFragment : Fragment() {
    private lateinit var binding: FragmentRecruterEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


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
        val foundationdate = binding.editProfileFounding
        val save = binding.editProfileSave
        val busType = binding.editProfileBustype
        val busSec = binding.editProfileBusSec
        val tax = binding.editProfileTaxnum

        name.isEnabled = false
        address.isEnabled = false
        phone.isEnabled = false
        foundationdate.isEnabled = false
        busType.isEnabled = false
        busSec.isEnabled = false
        tax.isEnabled = false

        binding.editProfileEditbtn.setOnClickListener {
            name.isEnabled = true
            address.isEnabled = true
            phone.isEnabled = true
            foundationdate.isEnabled = true
            busType.isEnabled = true
            busSec.isEnabled = true
            tax.isEnabled = true


            save.visibility = View.VISIBLE
        }
        save.setOnClickListener {

        }


    }
}