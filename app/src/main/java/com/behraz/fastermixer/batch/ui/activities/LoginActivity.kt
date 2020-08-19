package com.behraz.fastermixer.batch.ui.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.BuildConfig
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.enums.UserType
import com.behraz.fastermixer.batch.models.requests.behraz.UpdateResponse
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.activities.batch.BatchActivity
import com.behraz.fastermixer.batch.ui.activities.mixer.MixerActivity
import com.behraz.fastermixer.batch.ui.activities.pomp.PompActivity
import com.behraz.fastermixer.batch.ui.customs.fastermixer.NumericKeyboard
import com.behraz.fastermixer.batch.ui.dialogs.LocationPermissionDialog
import com.behraz.fastermixer.batch.ui.dialogs.NoNetworkDialog
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.*
import com.behraz.fastermixer.batch.viewmodels.LoginActivityViewModel
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity(), View.OnFocusChangeListener,
    PermissionHelper.Interactions,
    ApiService.InternetConnectionListener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        if (false) {
            startActivity(Intent(this, ContactActivity::class.java))
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
            //toast("هنوز پیاده سازی نشده است. لطفا از کیبرد گوشی خود استفاده کنید")
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


    private fun subscribeObservers() {
        viewModel.checkUpdateResponse.observe(this, Observer { event ->
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
                        if (ApiService.isNetworkAvailable()) {
                            viewModel.checkUpdates()
                        }
                        //TODO show proper message
                    }
                } else {
                    if (ApiService.isNetworkAvailable()) {
                        viewModel.checkUpdates()
                    }
                    //TODO show proper message
                }
            }
        })

        viewModel.loginResponse.observe(this, Observer { event ->
            if (!event.hasBeenHandled) {
                event.getEventIfNotHandled().let {
                    println("debug: loginResponse Observed")
                    btnLogin.showProgressBar(false)
                    if (it != null) {
                        if (it.isSucceed) {

                            if (checkBoxRememberMe.isChecked) {
                                shouldRememberCredential(true)
                            } else {
                                shouldRememberCredential(false)
                            }

                            if (it.entity!!.equipmentId == null) {
                                when (it.entity.userType) {
                                    UserType.Pomp -> toast(getString(R.string.driver_equipment_not_found))
                                    UserType.Mixer -> toast(getString(R.string.driver_equipment_not_found))
                                    UserType.Batch -> startActivity(
                                        Intent(
                                            this,
                                            ChooseEquipmentActivity::class.java
                                        )
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
        })
    }

    private fun shouldRememberCredential(shouldRemember: Boolean) {
        val prefs = getSharedPreferences(Constants.PREF_CREDENTIAL_NAME, Context.MODE_PRIVATE)
        if (shouldRemember) {
            prefs.edit().putBoolean(Constants.PREF_CREDENTIAL_REMEMBERED, true)
                .putString(
                    Constants.PREF_CREDENTIAL_FACTORY_ID,
                    etFactoryCode.text.trim().toString()
                )
                .putString(Constants.PREF_CREDENTIAL_USER, etUsername.text.trim().toString())
                .putString(Constants.PREF_CREDENTIAL_PASSWORD, etPassword.text.trim().toString())
                .apply()
        } else {
            prefs.edit()
                .clear()
                .putBoolean(Constants.PREF_CREDENTIAL_REMEMBERED, false)
                .apply()
        }
    }

    private fun loadCredentialIfExists() {
        val prefs = getSharedPreferences(Constants.PREF_CREDENTIAL_NAME, Context.MODE_PRIVATE)
        val remembered = prefs.getBoolean(Constants.PREF_CREDENTIAL_REMEMBERED, false)
        if (remembered) {
            checkBoxRememberMe.isChecked = true
            etFactoryCode.setText(prefs.getString(Constants.PREF_CREDENTIAL_FACTORY_ID, ""))
            etUsername.setText(prefs.getString(Constants.PREF_CREDENTIAL_USER, ""))
            etPassword.setText(prefs.getString(Constants.PREF_CREDENTIAL_PASSWORD, ""))
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

    override fun onPermissionsGranted() {
        if (ApiService.isNetworkAvailable())
            viewModel.checkUpdates()
    }

    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
        deniedPermissions.forEach { println("debug: $it denied") }
        toast("اجازه دسترسی داده نشد")
        finish()
    }

    override fun onInternetUnavailable() {
        NoNetworkDialog(this, R.style.my_alert_dialog).show()
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