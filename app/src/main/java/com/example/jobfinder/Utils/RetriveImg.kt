package com.example.jobfinder.Utils

import android.app.Activity
import android.net.Uri
import android.util.Log
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.jobfinder.R
import com.example.jobfinder.UI.UsersProfile.ProfileViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.grpc.Context

object RetriveImg {
    fun retrieveImage(
        userId: String,
        imgView: ImageView,
        activity: Activity,
        viewModel: ProfileViewModel
    ) {
        val storageReference: StorageReference = FirebaseStorage.getInstance().reference
        val imageRef: StorageReference = storageReference.child(userId)

        Glide.with(activity)
            .load(imageRef)
            .apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(imgView)
            .clearOnDetach()

        imageRef.downloadUrl
            .addOnSuccessListener { uri: Uri ->
                imgView.setBackgroundResource(R.drawable.image_loading_80px)
                // Trả về URI của hình ảnh thông qua giao diện hoặc cập nhật trạng thái của ViewModel
                viewModel.updateImageUri(uri)
                Glide.with(activity)
                    .load(uri)
                    .apply(RequestOptions.bitmapTransform(CircleCrop()))
                    .into(imgView)

            }
            .addOnFailureListener { exception ->
                Log.e("RetriveImg", "Failed to retrieve image: ${exception.message}")
                imgView.setBackgroundResource(R.drawable.profile)
            }
    }
}
