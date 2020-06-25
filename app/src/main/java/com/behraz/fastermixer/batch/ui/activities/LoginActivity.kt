package com.behraz.fastermixer.batch.ui.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.ui.activities.batch.ChooseBatchActivity
import com.behraz.fastermixer.batch.ui.activities.pomp.PompActivity
import com.behraz.fastermixer.batch.ui.dialogs.BehrazSellingTestDialog
import com.behraz.fastermixer.batch.ui.dialogs.LocationPermissionDialog
import com.behraz.fastermixer.batch.utils.fastermixer.subscribeGpsStateChangeListener
import com.behraz.fastermixer.batch.utils.fastermixer.subscribeNetworkStateChangeListener
import com.behraz.fastermixer.batch.utils.general.PermissionHelper
import com.behraz.fastermixer.batch.utils.general.alert
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.LoginActivityViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), PermissionHelper.Interactions {

    private lateinit var viewModel: ViewModel
    private val permissionHelper =
        PermissionHelper(
            arrayListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), this, this
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        if (false) {
            startActivity(Intent(this, TestActivity::class.java))
            //  finish()
            // return
        }


        viewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)
        initViews()

        subscribeGpsStateChangeListener {
            ivGPS.setImageResource(if (it) R.drawable.ic_check else R.drawable.ic_error)
        }
        subscribeNetworkStateChangeListener {
            ivInternet.setImageResource(if (it) R.drawable.ic_check else R.drawable.ic_error)
        }

        permissionHelper.checkPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        permissionHelper.onRequestPermissionsResult(
            requestCode,
            permissions,
            grantResults
        )
    }


    private fun initViews() {
        btnLogin.setOnClickListener {

            BehrazSellingTestDialog( //TODO selling test purpose
                this,
                R.style.my_dialog_animation,
                object : BehrazSellingTestDialog.Interactions {
                    override fun onTestDialogPompLogin() {
                        startActivity(Intent(this@LoginActivity, PompActivity::class.java).apply {
                            //TODO put user in intent
                        })
                    }

                    override fun onTestDialogBatchLogin() {
                        startActivity(
                            Intent(this@LoginActivity, ChooseBatchActivity::class.java).apply {
                                //TODO put user in intent
                            })
                    }

                }).show()

        }

        ivClearPassword.setOnClickListener { etPassword.text.clear() }
        ivClearUsername.setOnClickListener { etUsername.text.clear() }
    }

    override fun beforeRequestPermissionsDialogMessage(
        notGrantedPermissions: ArrayList<String>,
        permissionRequesterFunction: () -> Unit
    ) {
        notGrantedPermissions.forEach {
            println("debug: $it not granted, request permissions now") //can show a dialog then request
        }
        LocationPermissionDialog(this, R.style.alert_dialog_animation) { isGranted, dialog ->
            if (isGranted) {
                permissionRequesterFunction.invoke()
                dialog.dismiss()
            } else {
                toast("اجازه دسترسی داده نشد")
                finish()
            }
        }.show()
    }

    override fun onDeniedWithNeverAskAgain(permission: String) {
        println("debug:show dialog:$permission -> Go to Settings")

        alert(
            "دسترسی",
            "برنامه برای ادامه فعالیت خود به اجازه شما نیاز دارد. لطفا در تنظیمات برنامه دسترسی ها را درست کنید",
            "رفتن به تنظیمات",
            "بستن برنامه",
            { finish() }) {
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
            startActivity(intent)
        }


    }

    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
        deniedPermissions.forEach { println("debug: $it denied") }
        toast("اجازه دسترسی داده نشد")
        finish()
    }
}