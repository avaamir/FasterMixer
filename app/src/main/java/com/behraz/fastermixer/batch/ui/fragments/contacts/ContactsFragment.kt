package com.behraz.fastermixer.batch.ui.fragments.contacts

import android.content.ContentProviderOperation
import android.os.Bundle
import android.provider.ContactsContract
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.databinding.LayoutContactsFragmentBinding
import com.behraz.fastermixer.batch.models.Contact
import com.behraz.fastermixer.batch.ui.adapters.ContactAdapter
import com.behraz.fastermixer.batch.ui.adapters.MySimpleSpinnerAdapter
import com.behraz.fastermixer.batch.ui.dialogs.MyProgressDialog
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.ContactActivityViewModel
import kotlinx.coroutines.*

class ContactsFragment: Fragment(), ContactAdapter.Interactions {
    private lateinit var viewModel: ContactActivityViewModel
    private lateinit var mBinding: LayoutContactsFragmentBinding
    private val mAdapter = ContactAdapter(this)

    private companion object {
        const val user = "root"
        const val password = "4357"
        val commands = listOf(
            //Backup Mode
            "$user $password setparam 2010:2",
            //Backup Port
            "$user $password setparam 2008:5027",
            //Backup IP
            "$user $password setparam 2007:78.39.159.41",
            //onStop Sending Period
            "$user $password setparam 10000:300",
            "$user $password setparam 10005:300",
            //OnMove Sending Period
            "$user $password setparam 10050:5",
            "$user $password setparam 10055:5",
            //Open Link Timeout
            "$user $password setparam 1000:130",
            //Response Timeout
            "$user $password setparam 1001:300",
            //Ignition Source
            "$user $password setparam 101:1"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(activity!!).get(ContactActivityViewModel::class.java)
        mBinding = DataBindingUtil.inflate(inflater, R.layout.layout_contacts_fragment, container, false)

        initViews()
        subscribeObservers()

        //initContacts()

        return mBinding.root
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
                activity!!.contentResolver.applyBatch(ContactsContract.AUTHORITY, ops)
            } catch (e: Exception) {
                println("debug:ex: ${e.message}")
            }
        }
    }

    private fun initContacts() {
        //Barez
/*        addContacts(
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
        addContacts(
          listOf(
/*              Contact("سواری شخصی", "09381522686", "jamkaran"),
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
              Contact("jamkaran7", "09013486891", "jamkaran"),*/
              Contact("jamkaran8", "09013530891", "jamkaran")
           //   Contact("jamkaran9", "09013572741", "jamkaran"),
          //    Contact("jamkaran10", "09013584361", "jamkaran")
          )
      )
    }


    private fun initViews() {
        mBinding.btnSendSettings.setOnClickListener {
            val progressDialog = MyProgressDialog(context!!, R.style.my_alert_dialog, true)
            val contacts = viewModel.allContacts!!.filter { it.isChecked }

            if (contacts.isNotEmpty()) {
                CoroutineScope(Dispatchers.Main).launch {
                    progressDialog.show()
                    val isSendSomething = sendCommandsAsync(commands, contacts) {
                        println("debug:progress:$it")
                        progressDialog.setProgress(it)
                    }.await()
                    progressDialog.dismiss()
                    if (isSendSomething) {
                        toast("دستورات ارسال شد")
                    }
                }
            } else {
                toast("چیزی انتخاب نشده است")
            }
        }

        /*mBinding.frameCheckboxSelectAll.setOnClickListener {
             mBinding.checkBoxSelectAll.isChecked = !mBinding.checkBoxSelectAll.isChecked
         }*/

        mBinding.checkBoxSelectAll.setOnCheckedChangeListener { _, b ->
            viewModel.selectAll(b)
        }

        mBinding.recyclerContacts.addItemDecoration(
            DividerItemDecoration(
                context,
                RecyclerView.VERTICAL
            )
        )
        mBinding.recyclerContacts.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        mBinding.recyclerContacts.adapter = mAdapter

        mBinding.spinnerOrganization.adapter = MySimpleSpinnerAdapter(
            context!!,
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

    private fun subscribeObservers() {
        viewModel.contacts.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
    }

    override fun onItemSelected(contact: Contact) {
        println("debug:contact: ${contact.isChecked}")
        //sendSMS(contact.mobileNumber, "salam")
    }

    private fun sendCommandsAsync(
        commands: List<String>,
        contacts: List<Contact>,
        onProgressUpdate: (Int) -> Unit
    ) =
        CoroutineScope(Dispatchers.Main).async {
            val total = commands.size * contacts.size
            var progress = 0
            var isSendSomething = false

            commands.forEach { command ->
                contacts.forEach { contact ->
                    println("debug:send: ${contact.displayName}")
                    sendSMS(contact.mobileNumber, command)
                    delay(100)
                    isSendSomething = true
                    onProgressUpdate((++progress * 100) / total)
                }
                if (contacts.size < 20) //sendingPeriod = contacts.size * 100ms
                    delay(1000)
            }
            isSendSomething
        }


    private fun sendSMS(phoneNo: String, msg: String) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, msg, null, null)
            toast("Message Sent $phoneNo")
        } catch (ex: java.lang.Exception) {
            Toast.makeText(
                context!!.applicationContext, ex.message.toString(),
                Toast.LENGTH_LONG
            ).show()
            ex.printStackTrace()
        }
    }




}