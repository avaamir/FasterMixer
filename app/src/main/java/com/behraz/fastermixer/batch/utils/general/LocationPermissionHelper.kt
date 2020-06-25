package com.behraz.fastermixer.batch.utils.general

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


abstract class LocationPermissionHelper(
    private val activity: Activity,
    private val interactions: Interactions
) {

    abstract fun requiredPermissionAction()

    private companion object {
        const val GET_PERMISSIONS_REQ = 43
    }

    interface Interactions {
        fun beforeRequestPermissionDialogMessage(permissionRequesterFunction: () -> Unit) //if return true user accept if false user declined
        fun onPermissionDenied()
    }

    fun checkPermissionAndPickImage() {
        if (
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted
            // Should we show an explanation?
            if (
                ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                //send `requestPermissions` and wait for ui to call it if user accept dialog message
                interactions.beforeRequestPermissionDialogMessage(::requestPermissions)
            } else {
                // No explanation needed; request the permission
                requestPermissions()
            }
        } else { // Permission has already been granted
            requiredPermissionAction()
        }
    }


    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            GET_PERMISSIONS_REQ
        )
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == GET_PERMISSIONS_REQ) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requiredPermissionAction()
            } else {
                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                interactions.onPermissionDenied()
            }
        }
    }


}