package com.example.jobfinder.UI.UsersProfile

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.jobfinder.R
import com.example.jobfinder.databinding.ActivityProfileUploadImageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class profile_upload_image : AppCompatActivity() {
    private lateinit var binding: ActivityProfileUploadImageBinding
    private lateinit var auth: FirebaseAuth
    var fileUri: Uri? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileUploadImageBinding.inflate(layoutInflater)
        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid
        setContentView(binding.root)
        //back
        binding.uploadImageBackbtn.setOnClickListener(){
            back()
        }
        //choose image
        binding.profileImageChoose.setOnClickListener(){
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, ""), 0
            )

        }
        //save iamge to firebase
        binding.profileImageSave.setOnClickListener(){
            if (fileUri != null) {
                userId?.let {
                    uploadImage(userId)
                }

            } else {
                Toast.makeText(
                    applicationContext, R.string.profile_image_please_choose,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        //cancle
        binding.profileImageDiscard.setOnClickListener(){
            back()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
            try {
                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
                binding.profileImage.setImageBitmap(bitmap)
                binding.profileImage.setBackgroundColor(Color.BLACK)

            } catch (e: Exception) {
                Log.e("Exception", "Error: " + e)
            }
        }
    }
    private fun uploadImage(userid: String) {

        if (fileUri != null) {
            val progressDialog = ProgressDialog(this)
            progressDialog.setTitle(R.string.profile_image_uploading)
            progressDialog.setMessage(getString(R.string.profile_image_process))
            progressDialog.show()

            val ref: StorageReference = FirebaseStorage.getInstance().getReference()
                .child(userid)
            ref.putFile(fileUri!!).addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, R.string.profile_image_save_success, Toast.LENGTH_LONG)
                    .show()
            }.addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, R.string.profile_image_save_failed, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun back(){
        val resultIntent = Intent()
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }




}