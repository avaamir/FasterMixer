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
import com.behraz.fastermixer.batch.utils.general.createNewContact
import com.behraz.fastermixer.batch.utils.general.toast
import com.behraz.fastermixer.batch.viewmodels.ContactActivityViewModel
import kotlinx.coroutines.*

class ContactsFragment : Fragment(), ContactAdapter.Interactions {
    private lateinit var viewModel: ContactActivityViewModel
    private lateinit var mBinding: LayoutContactsFragmentBinding
    private val mAdapter = ContactAdapter(this)

    private companion object {
        const val user = "root"
        const val password = "4357"

        //160 taee, ye dune sms mishe
        /*
        *
        val teltonikaCommands = listOf(
            //Open Link Timeout
            "1000:30",
            //Response Timeout
            "1001:30",
            //Server IP
            //"2004:2.184.49.133",
            //Server Port
            //"2005:5027",
            //Backup Mode
            //"2010:2",
            //Backup Port
            //"2008:5027",
            //Backup IP
            //"2007:78.39.159.41",
            //onStop Sending Period
            "10000:300",
            "10005:300",

            //#Home----
            //OnMove Sending Period
            "10050:5",
            //Data Sending: Min Distance
            "10051:100",
            //Data Sending: Min Angle
            "10052:10",
            //Data Sending: Min Speed Delta
            "10053:10",
            //Recorded Data Sending Rate
            "10054:100",
            //Send Period
            "10055:5",
            //#Roaming
            //OnMove Sending Period
            "10150:5",
            //Data Sending: Min Distance
           // "10151:100",
            //Data Sending: Min Angle
            "10152:10",
            //Data Sending: Min Speed Delta
            "10153:10",
            //Recorded Data Sending Rate
            "10154:100",
            //Send Period
            "10155:5",
            //#Unknown
            //OnMove Sending Period
            /*"10250:5",
            //Data Sending: Min Distance
            "10251:100",
            //Data Sending: Min Angle
            "10252:10",
            //Data Sending: Min Speed Delta
            "10253:10",
            //Recorded Data Sending Rate
            "10254:100",
            //Send Period
            "10255:5",*/
            //----
            //Ignition Source
            //"101:1",
            //
            //Recorded Data Sending Order
            "1002:0" //0:newest 1:oldest
            //Reset CPU
            //"$user $password cpureset"
        )
        * */
        val teltonikaCommands = listOf(
            //Open Link Timeout
            /*"1000:30",
            //Response Timeout
            "1001:30",*/
            //Server IP
            "2004:78.39.159.41",
            //Server Port
            "2005:5027",
            //APN
            "2001:mtnirancell",
            //Backup Mode //0:Disabled, 2:Duplicated
            "2010:0",
            /*//Backup Port
            //"2008:5027",
            //Backup IP
            //"2007:78.39.159.41",
            //onStop Sending Period
            "10000:300",
            "10005:300",

            //#Home----
            //OnMove Sending Period
            "10050:10",
            //Data Sending: Min Distance
            "10051:200",
            //Data Sending: Min Angle
            "10052:20",
            //Data Sending: Min Speed Delta
            "10053:10",
            //Recorded Data Sending Rate
            "10054:1",
            //Send Period
            "10055:1",
            //#Roaming
            //OnMove Sending Period
            "10150:10",
            //Data Sending: Min Distance
            // "10151:100",
            //Data Sending: Min Angle
            "10152:20",
            //Data Sending: Min Speed Delta
            "10153:10",
            //Recorded Data Sending Rate
            "10154:1",
            //Send Period
            "10155:1",
            //#Unknown
            //OnMove Sending Period
            "10250:10",
            //Data Sending: Min Distance
            "10251:200",
            //Data Sending: Min Angle
            "10252:20",
            //Data Sending: Min Speed Delta
            "10253:10",
            //Recorded Data Sending Rate
            "10254:1",
            //Send Period
            "10255:1",
            //----
            //Ignition Source
            //"101:1",
            //
            //Recorded Data Sending Order
            "1002:0" //0:newest 1:oldest
            //Reset CPU
            //"$user $password cpureset"*/
        )

        /*fun createCommand(ids: List<Int>): String {
            if (ids.isNotEmpty()) {
                var result = "$user $password setparam "
                ids.forEach {
                    result += "${teltonikaCommands[it]};"
                }
                return result
            } else
                throw IllegalArgumentException("fuck you")
        }

        fun createCommand(vararg ids: Int) = createCommand(ids.toList())*/

        fun createCommand(commands: List<String>): List<String> {
            val commandList = arrayListOf<String>()
            if (commands.isNotEmpty()) {
                val SET_PARAM_COMMAND = "$user $password setparam "
                var result = SET_PARAM_COMMAND
                commands.forEachIndexed { index, command ->
                    if (index != commands.size - 1) {
                        if ((result.length + command.length + 1) <= 160) {
                            result += "$command;"
                        } else {
                            commandList.add(result)
                            result = "$SET_PARAM_COMMAND$command;"
                        }
                    } else {
                        result += "$command;"
                        commandList.add(result)
                    }
                }
                return commandList
            } else
                throw IllegalArgumentException("fuck you")
        }

        private val CONTACTS_JAMKARAN = listOf(
            Contact("میکسر 475ع36", "09038516423", "jamkaran", "359633102220394"),
            Contact("میکسر 637ع34", "09038516427", "jamkaran", "359633102227480"),
            Contact("میکسر 636ع34", "09038516523", "jamkaran", "359633102013302"),
            //TODO Contact("میکسر 22-355ع88", "***", "jamkaran", "358480088040573"),
            Contact("پمپ یک", "09038516537", "jamkaran", "359633102063083"),
            Contact("بنز تک 982ع18", "09038516548", "jamkaran", "359633102002594"),
            Contact("مایلر 655ع38", "09010885677", "jamkaran", "359633102074379"),
            Contact("Volvo L90F", "09010886081", "jamkaran", "359633102074536"),
            Contact("بنز تک 855ع45", "09014806687", "jamkaran", "359633100408579"),
            Contact("مایلر 987ع18", "09014808698", "jamkaran", "359633102016719"),
            Contact("بنز تک 132ع37", "09014808978", "jamkaran", "359633102232340"),
            Contact("بنز جرثقیل", "09013484661", "jamkaran", "359633102088114"),
            Contact("مزدا دوکابین", "09013486891", "jamkaran", "359633102062986"),
            Contact("لودر کوماتسو W90", "09013530891", "jamkaran", "359633100408553"),
            Contact("jamkaran9", "09013572741", "jamkaran", "359633102003386"),
            Contact("بلدوزر کوماتسو", "09013584361", "jamkaran", "359633102074171")
        )

        private val CONTACTS_BAREZ = listOf(
            Contact("بی ام سی زرد", "09925356408", "barez", "359633101175763"),
            Contact("بنز آبی اسمانی", "09925356405", "barez", "359633102559221"),
            Contact("بی ام سی سفید", "09160887340", "barez", "359633101094345"),
            Contact("barez4", "09921603892", "barez", "359633101093669"),
            Contact("پمپ", "09921603893", "barez", "359633101404106"),
            Contact("آمیکو سفید", "09925356431", "barez", "359633102259137"),
            Contact("کامیون", "09921603891", "barez", "359633101405244"),
            Contact("بنز زرد", "09160885890", "barez", "359633102599623"),
            Contact("بی ام سی سفید", "09921603894", "barez", "359633101828932"),
            Contact("بنزا سفید", "09160885872", "barez", "359633101440423")
        )

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel = ViewModelProvider(requireActivity()).get(ContactActivityViewModel::class.java)
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
            CONTACTS_BAREZ
        )

        //Jamkaran
        viewModel.insertContact(
            CONTACTS_JAMKARAN
        )

        requireContext().createNewContact(CONTACTS_JAMKARAN)
        requireContext().createNewContact(CONTACTS_BAREZ)
    }


    private fun initViews() {
        mBinding.etSearch.addTextChangedListener {
            viewModel.searchAndFilter(it?.trim().toString())
        }

        mBinding.btnSendSettings.setOnClickListener {
            val progressDialog = MyProgressDialog(requireContext(), R.style.my_alert_dialog, true)

            val allContacts = viewModel.allContacts.value
            if (allContacts != null) {
                val contacts = allContacts.filter {
                    viewModel.isCheckedMap[it.mobileNumber] == true
                }

                if (contacts.isNotEmpty()) {
                    CoroutineScope(Dispatchers.Main).launch {
                        progressDialog.show()
                        val isSendSomething =
                            sendCommandsAsync(createCommand(teltonikaCommands), contacts) {
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
            } else {
                toast("com.behraz.fastermixer.batch.models.requests.openweathermap.List is NULL")
            }
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
        viewModel.contacts.observe(viewLifecycleOwner) {
            mAdapter.submitList(it)
        }

        viewModel.organizations.observe(viewLifecycleOwner) {
            if ((it.size + 1) != mBinding.spinnerOrganization.adapter?.count) { //+1 be khater ezafe kardan `همه`
                mBinding.spinnerOrganization.adapter = MySimpleSpinnerAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_dropdown_item,
                    listOf("همه") + (it)
                )
            }
        }
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

    private fun sendCommandsAsync(
        commands: String,
        contacts: List<Contact>,
        onProgressUpdate: (Int) -> Unit
    ) =
        CoroutineScope(Dispatchers.Main).async {
            val total = contacts.size
            var progress = 0
            var isSendSomething = false

            contacts.forEach { contact ->
                sendSMS(contact.mobileNumber, commands)
                delay(100)
                isSendSomething = true
                onProgressUpdate((++progress * 100) / total)
            }
            if (contacts.size < 20) //sendingPeriod = contacts.size * 100ms
                delay(1000)

            isSendSomething
        }


    private fun sendSMS(phoneNo: String, msg: String) {
        try {
            val smsManager: SmsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(phoneNo, null, msg, null, null)
            toast("Message Sent $phoneNo")
        } catch (ex: java.lang.Exception) {
            Toast.makeText(
                requireContext().applicationContext, ex.message.toString(),
                Toast.LENGTH_LONG
            ).show()
            ex.printStackTrace()
        }
    }


}