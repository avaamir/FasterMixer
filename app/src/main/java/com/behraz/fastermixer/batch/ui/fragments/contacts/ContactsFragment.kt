package com.behraz.fastermixer.batch.ui.fragments.contacts

import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
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
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.ContactActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class ContactsFragment : Fragment(), ContactAdapter.Interactions {
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
            //Server IP
            //"$user $password setparam 2004:2.184.49.133",
            //Server Port
            //"$user $password setparam 2005:5027",
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(activity!!).get(ContactActivityViewModel::class.java)
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_contacts_fragment, container, false)

        initViews()
        subscribeObservers()

        //initContacts()

        return mBinding.root
    }

    private fun initContacts() {
        //Barez
        viewModel.insertContact(
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
        )

        //Jamkaran
        viewModel.insertContact(
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
        )
    }


    private fun initViews() {
        mBinding.etSearch.addTextChangedListener {
            viewModel.searchAndFilter(it?.trim().toString())
        }

        mBinding.btnSendSettings.setOnClickListener {
            toast("not yet implemented")
            /*val progressDialog = MyProgressDialog(context!!, R.style.my_alert_dialog, true)
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
            }*/
        }

        /*mBinding.frameCheckboxSelectAll.setOnClickListener {
             mBinding.checkBoxSelectAll.isChecked = !mBinding.checkBoxSelectAll.isChecked
         }*/

        mBinding.checkBoxSelectAll.setOnCheckedChangeListener { _, b ->
            viewModel.selectAll(b)
            mAdapter.notifyDataSetChanged()
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
                    viewModel.filterOrganization(mBinding.spinnerOrganization.selectedItem.toString())
                }

            }
    }

    private fun subscribeObservers() {
        viewModel.contacts.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })

        viewModel.organizations.observe(viewLifecycleOwner, Observer {
            if ((it.size + 1) != mBinding.spinnerOrganization.adapter?.count) { //+1 be khater ezafe kardan `همه`
                mBinding.spinnerOrganization.adapter = MySimpleSpinnerAdapter(
                    context!!,
                    android.R.layout.simple_spinner_dropdown_item,
                    listOf("همه") + (it)
                )
            }
        })

    }

    override fun onItemSelected(contact: Contact) {
        viewModel.selectContact(contact)
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