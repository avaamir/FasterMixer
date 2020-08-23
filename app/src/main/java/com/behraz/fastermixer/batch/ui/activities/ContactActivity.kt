package com.behraz.fastermixer.batch.ui.activities

import android.Manifest
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ActivityContactBinding
import com.behraz.fastermixer.batch.models.Contact
import com.behraz.fastermixer.batch.ui.dialogs.LocationPermissionDialog
import com.behraz.fastermixer.batch.ui.fragments.contacts.ContactsFragment
import com.behraz.fastermixer.batch.utils.general.PermissionHelper
import com.behraz.fastermixer.batch.utils.general.alert
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.ContactActivityViewModel


class ContactActivity : AppCompatActivity(),
    PermissionHelper.Interactions {
    private lateinit var viewModel: ContactActivityViewModel
    private lateinit var mBinding: ActivityContactBinding

    private companion object {
        const val REQ_GO_TO_SETTINGS_PERMISSION = 123
        const val CONTACTS_LIST_TAG = "contact-list-frag"
    }

    private val permissionHelper =
        PermissionHelper(
            arrayListOf(
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS,
                Manifest.permission.SEND_SMS,
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS
            ), this, this
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        viewModel = ViewModelProvider(this).get(ContactActivityViewModel::class.java)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact)

        if (viewModel.allContacts == null) {
            viewModel.allContacts = getContactList().also {
                it.forEach(::println)
            }
        }

        initFragments()

    }

    private fun initFragments() {
        supportFragmentManager.beginTransaction().apply {
            add(mBinding.container.id, ContactsFragment(), CONTACTS_LIST_TAG)
            commit()
        }

    }

    private fun getContactList(): ArrayList<Contact> {
        val contacts = HashMap<String, Contact>()

        val cur: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                //Getting contact Phone
                if (cur.getInt(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur: Cursor? = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    while (pCur != null && pCur.moveToNext()) {
                        val phoneNo: String =
                            pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        if (contacts[name] == null) {
                            contacts[name] =
                                Contact(
                                    name,
                                    phoneNo,
                                    ""
                                )
                            //println("debug:notNull $name, $phoneNo")
                        }
                    }
                    pCur?.close()
                }
                //Getting Contact Organization
                //1- Getting ContactRawID
                var rawContactId = "-1"
                contentResolver.query(
                    ContactsContract.RawContacts.CONTENT_URI,
                    arrayOf(ContactsContract.RawContacts._ID),
                    ContactsContract.RawContacts.CONTACT_ID + "= ?",
                    arrayOf(id),
                    null
                )?.apply {
                    if (moveToFirst()) {
                        rawContactId =
                            getInt(getColumnIndex(ContactsContract.RawContacts._ID)).toString()
                    }
                    close()
                }
                //2- Getting contact organization using rawContactId
                if (rawContactId != "-1") {
                    contentResolver.query(
                        ContactsContract.Data.CONTENT_URI,
                        null,
                        ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?",
                        arrayOf(
                            rawContactId,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
                        ),
                        null
                    )?.apply {
                        if (moveToFirst()) {
                            val organization =
                                getString(getColumnIndex(ContactsContract.CommonDataKinds.Organization.COMPANY))
                            contacts[name]?.company = organization
                        }
                        close()
                    }

                }
            }
        }
        cur?.close()
        return ArrayList(contacts.values)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQ_GO_TO_SETTINGS_PERMISSION) {
            permissionHelper.checkPermission()
        }
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

    override fun onPermissionsGranted() {}

    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
        deniedPermissions.forEach { println("debug: $it denied") }
        toast("اجازه دسترسی داده نشد")
        finish()
    }
}


