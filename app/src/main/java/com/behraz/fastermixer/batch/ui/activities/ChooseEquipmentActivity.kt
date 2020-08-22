package com.behraz.fastermixer.batch.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.app.FasterMixerApplication
import com.behraz.fastermixer.batch.models.Equipment
import com.behraz.fastermixer.batch.respository.apiservice.ApiService
import com.behraz.fastermixer.batch.ui.activities.batch.BatchActivity
import com.behraz.fastermixer.batch.ui.adapters.ChooseEquipmentAdapter
import com.behraz.fastermixer.batch.ui.customs.fastermixer.FasterMixerUserPanel
import com.behraz.fastermixer.batch.ui.dialogs.MyProgressDialog
import com.behraz.fastermixer.batch.ui.dialogs.NoNetworkDialog
import com.behraz.fastermixer.batch.ui.dialogs.RecordingDialogFragment
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.fastermixer.logoutAlertMessage
import com.behraz.fastermixer.batch.utils.general.*
import com.behraz.fastermixer.batch.viewmodels.ChooseEquipmentActivityViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_choose_batch.*

class ChooseEquipmentActivity : AppCompatActivity(), FasterMixerUserPanel.Interactions,
    ChooseEquipmentAdapter.Interaction, ApiService.OnUnauthorizedListener, ApiService.InternetConnectionListener {

    private var snackbar: Snackbar? = null
    private lateinit var viewModel: ChooseEquipmentActivityViewModel
    private var mAdapter = ChooseEquipmentAdapter(this)
    private val progressDialog: MyProgressDialog by lazy {
        MyProgressDialog(this, R.style.my_alert_dialog)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_batch)

        viewModel = ViewModelProvider(this).get(ChooseEquipmentActivityViewModel::class.java)



        initViews()
        subscribeObservers()
        viewModel.getEquipments()

        subscribeNetworkStateChangeListener { userPanel.setInternetState(it) }
        subscribeGpsStateChangeListener { userPanel.setGPSState(it) }
        if (FasterMixerApplication.isDemo) {
            layoutDemo.visibility = View.VISIBLE
        }
    }

    private fun subscribeObservers() {
        viewModel.equipments.observe(this, Observer {
            progressBar.visibility = View.GONE
            if (it != null) {
                if (it.isSucceed) {
                    mAdapter.submitList(it.entity)
                } else {
                    toast(it.message)
                }
            } else {
                snackbar?.dismiss()
                //  Handler().postDelayed({
                this.snackbar = snack(Constants.SERVER_ERROR) {
                    progressBar.visibility = View.VISIBLE
                    viewModel.getEquipments()
                }
                //   }, 500)
            }
        })


        viewModel.chooseEquipmentResponse.observe(this, Observer {
            progressDialog.dismiss()
            if (it != null) {
                if (it.isSucceed) {
                    println("debugx: ViewLayer: BatchActivity Called..")
                    startActivity(Intent(this, BatchActivity::class.java))
                    finish()
                } else {
                    //TODO
                }
            } else {
                snack(Constants.SERVER_ERROR) {
                    progressDialog.show()
                    viewModel.chooseEquipment(retryLastRequest = true)
                }
            }
        })


        viewModel.user.observe(this, Observer {
            it?.let {
                userPanel.setUsername(it.name)
                userPanel.setPersonalCode(it.personalCode)
            }
        })

        viewModel.logoutResponse.observe(this, Observer {
            progressDialog.dismiss()
            if (it != null) {
                if (it.isSucceed) {
                    toast("خروج موفقیت آمیز بود")
                    finish()
                } else {
                    toast(it.message)
                }
            } else {
                snackbar?.dismiss()
                snackbar = snack(Constants.SERVER_ERROR) {
                    progressDialog.show()
                    viewModel.logout()
                }
            }
        })
    }

    private fun initViews() {
        userPanel.setInteractions(this)
        batchRecycler.adapter = mAdapter
        batchRecycler.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onLogoutClicked(view: View) {
        logoutAlertMessage {
            viewModel.logout()
            progressDialog.show()
        }
    }

    override fun onRecordClicked(btnRecord: FloatingActionButton?) {
        RecordingDialogFragment().show(supportFragmentManager, null)
    }

    override fun onCallClicked(view: View) {
        toast("not yet implemented")
    }

    override fun onItemClicked(item: Equipment) {
        if (!item.isAvailable) {
            toast("این بچ درحال استفاده توسط کاربر دیگری میباشد. در صورت وجود مشکل با هماهنگ کننده تماس بگیرید")
        } else {
            viewModel.chooseEquipment(item.id)
            progressDialog.show()
        }

    }

    override fun onUnauthorizedAction(event: Event<Unit>) {
        toast("شما نیاز به ورود مجدد دارید")
        finish()
    }

    override fun onInternetUnavailable() {
        NoNetworkDialog(this, R.style.my_alert_dialog).show()
    }

}