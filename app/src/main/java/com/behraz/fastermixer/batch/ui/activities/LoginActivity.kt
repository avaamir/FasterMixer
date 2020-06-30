package com.behraz.fastermixer.batch.ui.activities

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.enums.UserType
import com.behraz.fastermixer.batch.ui.activities.batch.BatchActivity
import com.behraz.fastermixer.batch.ui.activities.pomp.PompActivity
import com.behraz.fastermixer.batch.ui.dialogs.LocationPermissionDialog
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.fastermixer.subscribeGpsStateChangeListener
import com.behraz.fastermixer.batch.utils.fastermixer.subscribeNetworkStateChangeListener
import com.behraz.fastermixer.batch.utils.general.*
import com.behraz.fastermixer.batch.viewmodels.LoginActivityViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), PermissionHelper.Interactions {

    companion object {
        private const val REQ_GO_TO_SETTINGS_PERMISSION = 12
    }


    private lateinit var viewModel: LoginActivityViewModel
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
        observeViewModel()

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
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            when {
                username.isEmpty() -> {
                    toast("لطفا نام کاربری خود را وارد کنید", false)
                }
                password.isEmpty() -> {
                    toast("لطفا گذرواژه خود را وارد کنید", false)
                }
                else -> {
                    btnLogin.showProgressBar(true)
                    viewModel.login(username, password)
                }
            }
            //TODO test purpose
            /*BehrazSellingTestDialog( //TODO selling test purpose
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

                }).show() */
        }

        ivClearPassword.setOnClickListener { etPassword.text.clear() }
        ivClearUsername.setOnClickListener { etUsername.text.clear() }

        //TODO TEST purpose
        etUsername.setText("ali")
        etPassword.setText("12345")
    }


    private fun observeViewModel() {
        viewModel.loginResponse.observe(this, Observer {
            btnLogin.showProgressBar(false)
            if (it != null) {
                if (it.isSucceed) {
                    if (it.entity.equipmentId == null) {
                        if (it.entity.userType == UserType.Mixer) {
                            toast("کاربر میسکر در این نسخه از برنامه تعریف نشده است. لطفا برنامه را به روز رسانی کنید")
                            return@Observer
                        }
                        startActivity(Intent(this, ChooseEquipmentActivity::class.java))
                    } else {
                        when (it.entity.userType) {
                            UserType.Pomp -> startActivity(Intent(this, PompActivity::class.java))
                            UserType.Mixer -> toast("کاربر میسکر در این نسخه از برنامه تعریف نشده است. لطفا برنامه را به روز رسانی کنید")
                            UserType.Batch -> startActivity(Intent(this, BatchActivity::class.java))
                        }.exhaustive()
                    }
                } else {
                    toast(it.message)
                }
            } else {
                snack(Constants.SERVER_ERROR) {
                    btnLogin.showProgressBar(true)
                    viewModel.tryAgain()
                }
            }
        })
    }


    //get permission

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_GO_TO_SETTINGS_PERMISSION) {
            permissionHelper.checkPermission()
        }
    }

    override fun beforeRequestPermissionsDialogMessage(
        notGrantedPermissions: ArrayList<String>,
        permissionRequesterFunction: () -> Unit
    ) {
        notGrantedPermissions.forEach {
            println("debug: $it not granted, request permissions now") //can show a dialog then request
        }
        LocationPermissionDialog(this, R.style.my_alert_dialog) { isGranted, dialog ->
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
            title = "دسترسی",
            message = "برنامه برای ادامه فعالیت خود به اجازه شما نیاز دارد. لطفا در تنظیمات برنامه دسترسی ها را درست کنید",
            positiveButtonText = "رفتن به تنظیمات",
            negativeButtonText = "بستن برنامه",
            isCancelable = false,
            onNegativeClicked = { finish() }
        ) {
            val intent = Intent().apply {
                action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                addCategory(Intent.CATEGORY_DEFAULT)
                data = Uri.parse("package:$packageName")
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            }
            startActivityForResult(intent, REQ_GO_TO_SETTINGS_PERMISSION)
        }


    }

    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
        deniedPermissions.forEach { println("debug: $it denied") }
        toast("اجازه دسترسی داده نشد")
        finish()
    }
}