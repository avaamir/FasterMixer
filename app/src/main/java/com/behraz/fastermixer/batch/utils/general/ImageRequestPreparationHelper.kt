package com.behraz.fastermixer.batch.utils.general

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import id.zelory.compressor.Compressor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class ImageRequestPreparationHelper(
    private val activity: Activity,
    private val interactions: Interactions
) {

    private companion object {
        const val PICK_IMAGE_REQ = 123
        const val GET_PERMISSIONS_REQ = 423
    }


    interface Interactions {
        fun onImageNotChosen()
        fun onImageNotFound()
        fun beforeRequestPermissionDialogMessage(permissionRequesterFunction: () -> Unit) //if return true user accept if false user declined
        fun onPermissionDenied()
        fun onImageReady(compressedFile: File, compressedBitmap: Bitmap)
    }


    fun checkPermissionAndPickImage() {
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                interactions.beforeRequestPermissionDialogMessage(::requestPermissions)
                //send `requestPermissions` and wait for ui to call it if user accept dialog message
            } else {
                // No explanation needed; request the permission
                requestPermissions()
            }
        } else { // Permission has already been granted
            pickImage()
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            GET_PERMISSIONS_REQ
        )
    }

    private fun requestChangeProfilePic(imagePath: String?) {
        if (imagePath == null) {
            interactions.onImageNotChosen()
            return
        }
        val file = File(imagePath)
        if (!file.exists()) {
            interactions.onImageNotFound()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val compressedImageFile = Compressor.compress(activity, file)
            val compressedBitmap = BitmapFactory.decodeFile(compressedImageFile.absolutePath)

            withContext(Dispatchers.Main) {
                interactions.onImageReady(compressedImageFile, compressedBitmap)
            }
        }
    }

    private fun pickImage() {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(galleryIntent,
            PICK_IMAGE_REQ
        )
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // When an Image is picked
        if (requestCode == PICK_IMAGE_REQ && resultCode == Activity.RESULT_OK && data != null) {
            // Get the Image from data
            val selectedImage = data.data
            val imagePath: String = FileUtils.getImagePath(activity, selectedImage)
            requestChangeProfilePic(imagePath)
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == GET_PERMISSIONS_REQ) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImage()
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                interactions.onPermissionDenied()
            }
        }
    }

}
