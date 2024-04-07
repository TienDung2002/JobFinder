package com.example.jobfinder.UI.UsersProfile

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.jobfinder.R
import com.example.jobfinder.UI.SplashScreen.SelectRoleActivity
import com.example.jobfinder.databinding.FragmentUserProfileMenuBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class UserProfileMenuFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileMenuBinding
    private lateinit var auth: FirebaseAuth
    lateinit var viewModel: ProfileViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        // hiển thị username
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid
        userId?.let { userId ->
            database.child("UserBasicInfo").child(userId).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userName = snapshot.child("name").getValue(String::class.java)
                    userName?.let {
                        binding.userName.text = it
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("UserProfileMenuFragment", "Database error: ${error.message}")
                }
            })

            retrieveImage(userId)
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileMenuBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //account
        binding.profileAccount.setOnClickListener {
            val intent = Intent(requireContext(), AccountActivity::class.java)
            startActivity(intent)

        }

        //logout
        binding.profileLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(context, SelectRoleActivity::class.java))
            Toast.makeText(context, "Đăng xuất thành công", Toast.LENGTH_SHORT).show()
            requireActivity().finish()

        }


    }
    private fun retrieveImage(userid : String) {
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference
        val imageRef: StorageReference = storageReference.child(userid)
        Log.d("SeekerEditProfileFragment", "ImageRef path: $imageRef")


        imageRef.downloadUrl
            .addOnSuccessListener { uri: Uri ->
                binding.profileImage.setBackgroundResource(R.drawable.image_loading_80px)
                viewModel.imageUri = uri
                Glide.with(requireContext())
                    .load(uri)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(binding.profileImage)

            }
            .addOnFailureListener { exception ->
                Log.e("UserProfileMenuFragment", "Failed to retrieve image: ${exception.message}")
                binding.profileImage.setBackgroundResource(R.drawable.profile)

            }
    }





}