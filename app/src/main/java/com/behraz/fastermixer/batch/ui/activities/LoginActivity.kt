package com.behraz.fastermixer.batch.ui.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.BuildConfig
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.app.FasterMixerApplication
import com.behraz.fastermixer.batch.app.receivers.isNetworkAvailable
import com.behraz.fastermixer.batch.models.enums.UserType
import com.behraz.fastermixer.batch.models.requests.behraz.UpdateResponse
import com.behraz.fastermixer.batch.ui.activities.admin.AdminActivity
import com.behraz.fastermixer.batch.ui.activities.batch.BatchActivity
import com.behraz.fastermixer.batch.ui.activities.mixer.MixerActivity
import com.behraz.fastermixer.batch.ui.activities.pomp.PompActivity
import com.behraz.fastermixer.batch.ui.customs.fastermixer.NumericKeyboard
import com.behraz.fastermixer.batch.ui.dialogs.LocationPermissionDialog
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.*
import com.behraz.fastermixer.batch.viewmodels.LoginActivityViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.gps_internet_status_icons.*


class LoginActivity : AppCompatActivity(), View.OnFocusChangeListener,
    PermissionHelper.Interactions {

    companion object {
        private const val REQ_GO_TO_SETTINGS_PERMISSION = 12
        private var focusedEditTextId: Int? = null
    }

    private var focusedEditText: EditText? = null
        set(value) {
            field = value
            focusedEditTextId = value?.id
        }


    private lateinit var viewModel: LoginActivityViewModel
    private val permissionHelper =
        PermissionHelper(
            arrayListOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO
            ), this, this
        )

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (false) {
            startActivity(Intent(this, TestActivity::class.java))
            finish()
            return
        }

        viewModel = ViewModelProvider(this).get(LoginActivityViewModel::class.java)
        initViews()
        subscribeObservers()

        subscribeGpsStateChangeListener {
            ivGPS.setImageResource(if (it) R.drawable.ic_check else R.drawable.ic_error)
        }
        subscribeNetworkStateChangeListener {
            ivInternet.setImageResource(if (it) R.drawable.ic_check else R.drawable.ic_error)
            if (it && permissionHelper.arePermissionsGranted()) {
                viewModel.checkUpdates()
            }
        }

        //TODO UI Test Purpose
        //imageView5?.setOnClickListener { startActivity(Intent(this, AdminActivity::class.java)) }

        if(permissionHelper.arePermissionsGranted()) {
            (application as FasterMixerApplication).registerLocationUpdaterIfNeeded()
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

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        checkBoxRememberMe.isChecked = true

        tvVersion.text = "v${BuildConfig.VERSION_NAME}"

        loadCredentialIfExists()

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            /*TODO momkene niaz bashe Factory Code bardashte beshe, dar in soorat bayad dar NumericKeyBoard va Listener inja taghirati ijad beshavad*/
            etFactoryCode.disableKeyboardFromAppearing()
            etUsername.disableKeyboardFromAppearing()
            etPassword.disableKeyboardFromAppearing()
            etFactoryCode.onFocusChangeListener = this
            etUsername.onFocusChangeListener = this
            etPassword.onFocusChangeListener = this
        } else {
            etFactoryCode.enableKeyboardAppearing()
            etUsername.enableKeyboardAppearing()
            etPassword.enableKeyboardAppearing()
            etFactoryCode.onFocusChangeListener = null
            etUsername.onFocusChangeListener = null
            etPassword.onFocusChangeListener = null
            focusedEditTextId?.run {
                showSoftKeyboard(findViewById(this))
            }
        }



        numericKeyboard?.setInteractions {
            when (it) {
                NumericKeyboard.State.Password.name -> {
                    etPassword.requestFocus()
                }
                NumericKeyboard.State.Username.name -> {
                    etUsername.requestFocus()
                }
                NumericKeyboard.State.FactoryCode.name -> {
                    etFactoryCode.requestFocus()
                }
                "backspace" -> focusedEditText?.text?.let { editable ->
                    if (editable.isNotEmpty()) {
                        editable.delete(
                            editable.length - 1,
                            editable.length
                        )
                    }
                }
                else -> {
                    focusedEditText?.append(it)
                }
            }
        }


        btnLogin.setOnClickListener {
            val factoryCode = etFactoryCode.text.toString()
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            when {
                factoryCode.isEmpty() -> {
                    toast("لطفا شناسه شرکت خود را وارد کنید", false)
                }
                username.isEmpty() -> {
                    toast("لطفا نام کاربری خود را وارد کنید", false)
                }
                password.isEmpty() -> {
                    toast("لطفا گذرواژه خود را وارد کنید", false)
                }
                else -> {
                    btnLogin.showProgressBar(true)
                    viewModel.login(username, password, factoryCode)
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

        ivClearFactory.setOnClickListener { etFactoryCode.text.clear() }
        ivClearPassword.setOnClickListener { etPassword.text.clear() }
        ivClearUsername.setOnClickListener { etUsername.text.clear() }

        //TODO TEST purpose
        //Pomp
        // etUsername.setText("mahdi")
        //  etPassword.setText("123456789")

        //Batch
        /*etUsername.setText("sara")
        etPassword.setText("1234")*/

        if (etFactoryCode.visibility != View.GONE)
            etFactoryCode.requestFocus()
        else
            etUsername.requestFocus()

    }

    private fun loadCredentialIfExists() {
        viewModel.getUserCredentials { factoryId, username, password ->
            checkBoxRememberMe.isChecked = true
            etFactoryCode.setText(factoryId)
            etUsername.setText(username)
            etPassword.setText(password)
        }
    }


    private fun subscribeObservers() {
        viewModel.checkUpdateResponse.observe(this) { event ->
            event.getEventIfNotHandled()?.let {
                if (it.entity !== UpdateResponse.NoResponse) {
                    if (it.isSucceed) {
                        if (it.entity!!.version > BuildConfig.VERSION_CODE) {
                            startActivity(Intent(this, UpdateActivity::class.java).also { intent ->
                                intent.putParcelableExtra(
                                    Constants.INTENT_UPDATE_ACTIVITY_UPDATE_RESPONSE,
                                    it.entity
                                )
                            })
                            finish()
                        }
                    } else {
                        if (isNetworkAvailable()) {
                            Handler().postDelayed({ viewModel.checkUpdates() }, 5000)

                        }
                        //TODO show proper message
                    }
                } else {
                    if (isNetworkAvailable()) {
                        Handler().postDelayed({ viewModel.checkUpdates() }, 5000)
                    }
                    //TODO show proper message
                }
            }
        }

        viewModel.loginResponse.observe(this) { event ->
            if (!event.hasBeenHandled) {
                event.getEventIfNotHandled().let {
                    println("debug: loginResponse Observed")
                    btnLogin.showProgressBar(false)
                    if (it != null) {
                        if (it.isSucceed) {
                            if (checkBoxRememberMe.isChecked) {
                                viewModel.saveUserCredential(
                                    etFactoryCode.text.trim().toString(),
                                    etUsername.text.trim().toString(),
                                    etPassword.text.trim().toString()
                                )
                            } else {
                                viewModel.clearUserCredentials()
                            }

                            if (it.entity!!.equipmentId == null) {
                                when (it.entity.userType) {
                                    UserType.Pomp, UserType.Mixer -> toast(getString(R.string.driver_equipment_not_found))
                                    UserType.Batch -> startActivity(
                                        Intent(this, ChooseEquipmentActivity::class.java)
                                    )
                                    UserType.Admin -> startActivity(
                                        Intent(this, AdminActivity::class.java)
                                    )
                                }.exhaustive()
                            } else {
                                when (it.entity.userType) {
                                    UserType.Pomp -> startActivity(
                                        Intent(
                                            this,
                                            PompActivity::class.java
                                        )
                                    )
                                    UserType.Mixer -> startActivity(
                                        Intent(
                                            this,
                                            MixerActivity::class.java
                                        )
                                    )
                                    UserType.Batch -> startActivity(
                                        Intent(
                                            this,
                                            BatchActivity::class.java
                                        )
                                    )
                                    UserType.Admin -> startActivity(
                                        Intent(
                                            this,
                                            AdminActivity::class.java
                                        )
                                    )
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
                }
            }
        }
    }

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

    @SuppressLint("MissingPermission")
    override fun onPermissionsGranted() {
        if (isNetworkAvailable())
            viewModel.checkUpdates()
        (application as FasterMixerApplication).registerLocationUpdaterIfNeeded()
    }

    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
        deniedPermissions.forEach { println("debug: $it denied") }
        toast("اجازه دسترسی داده نشد")
        finish()
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus) {
            focusedEditText = view as EditText
            when (view.id) {
                etFactoryCode.id -> numericKeyboard?.setState(NumericKeyboard.State.FactoryCode)
                etUsername.id -> numericKeyboard?.setState(NumericKeyboard.State.Username)
                etPassword.id -> numericKeyboard?.setState(NumericKeyboard.State.Password)
            }
        }
    }


}