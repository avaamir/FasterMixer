package com.behraz.fastermixer.batch.ui.activities

import android.Manifest
import android.content.ContentProviderOperation
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.telephony.SmsManager
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.ActivityContactBinding
import com.behraz.fastermixer.batch.models.Contact
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.adapters.ContactAdapter
import com.behraz.fastermixer.batch.ui.adapters.MySimpleSpinnerAdapter
import com.behraz.fastermixer.batch.ui.dialogs.LocationPermissionDialog
import com.behraz.fastermixer.batch.ui.dialogs.MyProgressDialog
import com.behraz.fastermixer.batch.ui.fragments.contacts.ContactsFragment
import com.behraz.fastermixer.batch.utils.general.PermissionHelper
import com.behraz.fastermixer.batch.utils.general.alert
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.ContactActivityViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main


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

        //Barez
        /*addContacts(
            listOf(
                Contact("barez1", "09925356408", "barez"),
                Contact("barez2", "09925356405", "barez"),
                Contact("barez3", "09160887340", "barez"),
                Contact("barez4", "09921603892", "barez"),
                Contact("barez5", "09921603893", "barez"),
                Contact("barez6", "09925356431", "barez"),
                Contact("barez7", "09921603891", "barez"),
                Contact("barez8", "09160885890", "barez"),
                Contact("barez9", "09921603894", "barez"),
                Contact("barez10", "09160885872", "barez")
            )
        )*/

        //Jamkaran
        /*addContacts(
          listOf(
              Contact("سواری شخصی", "09381522686", "jamkaran"),
              Contact("میکسر یک", "09038516423", "jamkaran"),
              Contact("میکسر دو", "09038516427", "jamkaran"),
              Contact("میکسر سه", "09038516523", "jamkaran"),
              Contact("پمپ 1", "09038516537", "jamkaran"),
              Contact("کمپرسی 1", "09038516548", "jamkaran"),
              Contact("jamkaran1", "09010885677", "jamkaran"),
              Contact("jamkaran2", "09010886081", "jamkaran"),
              Contact("jamkaran3", "09014806687", "jamkaran"),
              Contact("jamkaran4", "09014808698", "jamkaran"),
              Contact("jamkaran5", "09014808978", "jamkaran"),
              Contact("jamkaran6", "09013484661", "jamkaran"),
              Contact("jamkaran7", "09013486891", "jamkaran"),
              Contact("jamkaran8", "09013530891", "jamkaran"),
              Contact("jamkaran9", "09013572741", "jamkaran"),
              Contact("jamkaran10", "09013584361", "jamkaran")
          )
      )*/


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


    // Creates a contact entry from the current UI values, using the currently-selected account.
    private fun addContacts(contacts: List<Contact>) {
        contacts.forEach { contact ->
            val ops = ArrayList<ContentProviderOperation>()
            contact.run {
                ops.add(
                    ContentProviderOperation.newInsert(
                        ContactsContract.RawContacts.CONTENT_URI
                    )
                        .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                        .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                        .build()
                )


                //------------------------------------------------------ Names

                ops.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                        )
                        .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            displayName
                        ).build()
                )


                //------------------------------------------------------ Mobile Number

                ops.add(
                    ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                        .withValue(
                            ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                        )
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, mobileNumber)
                        .withValue(
                            ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                        )
                        .build()
                )


                //------------------------------------------------------ Home Numbers
                if (homeNumber != null) {
                    ops.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                            )
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, homeNumber)
                            .withValue(
                                ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_HOME
                            )
                            .build()
                    )
                }

                //------------------------------------------------------ Work Numbers
                if (workNumber != null) {
                    ops.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                            )
                            .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, workNumber)
                            .withValue(
                                ContactsContract.CommonDataKinds.Phone.TYPE,
                                ContactsContract.CommonDataKinds.Phone.TYPE_WORK
                            )
                            .build()
                    )
                }


                //------------------------------------------------------ Email
                if (emailID != null) {
                    ops.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE
                            )
                            .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                            .withValue(
                                ContactsContract.CommonDataKinds.Email.TYPE,
                                ContactsContract.CommonDataKinds.Email.TYPE_WORK
                            )
                            .build()
                    )
                }

                //------------------------------------------------------ Organization
                if (company != "" && jobTitle != "") {
                    ops.add(
                        ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                            .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                            .withValue(
                                ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE
                            )
                            .withValue(
                                ContactsContract.CommonDataKinds.Organization.COMPANY,
                                company
                            )
                            .withValue(
                                ContactsContract.CommonDataKinds.Organization.TYPE,
                                ContactsContract.CommonDataKinds.Organization.TYPE_WORK
                            )
                            .withValue(
                                ContactsContract.CommonDataKinds.Organization.TITLE,
                                jobTitle
                            )
                            .withValue(
                                ContactsContract.CommonDataKinds.Organization.TYPE,
                                ContactsContract.CommonDataKinds.Organization.TYPE_WORK
                            )
                            .build()
                    )
                }
            }

            // Asking the Contact provider to create a new contact
            try {
                contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            } catch (e: Exception) {
                println("debug:ex: ${e.message}")
            }
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
            startActivityForResult(intent, ContactActivity.REQ_GO_TO_SETTINGS_PERMISSION)
        }


    }

    override fun onPermissionsGranted() {}

    override fun onPermissionDenied(deniedPermissions: ArrayList<String>) {
        deniedPermissions.forEach { println("debug: $it denied") }
        toast("اجازه دسترسی داده نشد")
        finish()
    }
}


