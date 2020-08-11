package com.behraz.fastermixer.batch.ui.activities

import android.content.ContentProviderOperation
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
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
import com.behraz.fastermixer.batch.ui.adapters.ContactAdapter
import com.behraz.fastermixer.batch.ui.adapters.MySimpleSpinnerAdapter
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.ContactActivityViewModel


class ContactActivity : AppCompatActivity(), ContactAdapter.Interactions {
    private lateinit var viewModel: ContactActivityViewModel
    private lateinit var mBinding: ActivityContactBinding
    private val mAdapter = ContactAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)

        viewModel = ViewModelProvider(this).get(ContactActivityViewModel::class.java)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_contact)

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
        if (viewModel.allContacts == null) {
            viewModel.allContacts = getContactList().also {
                it.forEach(::println)
            }
        }

        initViews()
        subscribeObservers()
    }

    private fun subscribeObservers() {

        viewModel.contacts.observe(this, Observer {
            mAdapter.submitList(it)
        })
    }

    private fun initViews() {

        mBinding.btnSendSettings.setOnClickListener {
            var isSendSomething = false
            viewModel.allContacts!!.forEach {
                if (it.isChecked) {
                    println("debug:send: $it")
                    sendSMS(it.mobileNumber, "root 4357 setparam 2008:5027")
                    isSendSomething = true
                }
            }
            if (isSendSomething)
                toast("ارسال شد", false)
        }

        /*mBinding.frameCheckboxSelectAll.setOnClickListener {
             mBinding.checkBoxSelectAll.isChecked = !mBinding.checkBoxSelectAll.isChecked
         }*/

        mBinding.checkBoxSelectAll.setOnCheckedChangeListener { _, b ->
            viewModel.selectAll(b)
        }

        mBinding.recyclerContacts.addItemDecoration(
            DividerItemDecoration(
                this,
                RecyclerView.VERTICAL
            )
        )
        mBinding.recyclerContacts.layoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        mBinding.recyclerContacts.adapter = mAdapter

        mBinding.spinnerOrganization.adapter = MySimpleSpinnerAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            listOf("همه") + (viewModel.allContacts!!.map { it.company }.distinct())
        )

        mBinding.spinnerOrganization.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parentView: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parentView: AdapterView<*>?,
                    selectedItemView: View?,
                    position: Int,
                    id: Long
                ) {
                    viewModel.filterContactsByOrganization(mBinding.spinnerOrganization.selectedItem.toString())
                }

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

    override fun onItemSelected(contact: Contact) {
        println("debug:contact: ${contact.isChecked}")
        //sendSMS(contact.mobileNumber, "salam")
    }

    private fun sendSMS(phoneNo: String, msg: String) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, msg, null, null)
            toast("Message Sent $phoneNo")
        } catch (ex: java.lang.Exception) {
            Toast.makeText(
                applicationContext, ex.message.toString(),
                Toast.LENGTH_LONG
            ).show()
            ex.printStackTrace()
        }
    }
}


