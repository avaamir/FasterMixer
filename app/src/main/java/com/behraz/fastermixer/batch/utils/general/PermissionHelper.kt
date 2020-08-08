package com.behraz.fastermixer.batch.utils.general

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class PermissionHelper(
    private val permissions: ArrayList<String>,
    private val activity: Activity,
    private val interactions: Interactions
) {

    private companion object {
        const val GET_PERMISSIONS_REQ = 423
    }


    interface Interactions {

        /** show description dialog, why you need permissions, then invoke `permissionRequesterFunction` **/
        fun beforeRequestPermissionsDialogMessage(
            notGrantedPermissions: ArrayList<String>,
            permissionRequesterFunction: () -> Unit
        )

        /**it's according to app policy, you can req again, can show a dialog or can continue without permission..**/
        fun onPermissionDenied(deniedPermissions: ArrayList<String>)

        /**navigate user to settings**/
        fun onDeniedWithNeverAskAgain(permission: String)

        fun onPermissionsGranted()
    }


    fun checkPermission() {
        val deniedPermissions = ArrayList<String>()
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                deniedPermissions.add(permission)
            }
        }
        if (deniedPermissions.isNotEmpty()) {
            interactions.beforeRequestPermissionsDialogMessage(
                deniedPermissions,
                ::requestPermissions
            )
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            activity,
            permissions.toTypedArray(),
            GET_PERMISSIONS_REQ
        )
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // If request is cancelled, the result arrays are empty.
        if (requestCode == GET_PERMISSIONS_REQ) {

            val deniedPermissions = ArrayList<String>()
            for (i in permissions.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            activity,
                            permissions[i]
                        )
                    ) {
                        interactions.onDeniedWithNeverAskAgain(permissions[i])
                        // Do something if permission is not granted and the user has also checked the **"Don't ask again"**
                    } else {
                        deniedPermissions.add(permissions[i])
                    }
                }
            }
            if (deniedPermissions.isNotEmpty()) {
                interactions.onPermissionDenied(deniedPermissions)
            } else {
                interactions.onPermissionsGranted()
            }

        }
    }

    fun arePermissionsGranted(): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

}
