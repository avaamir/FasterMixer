package com.behraz.fastermixer.batch.ui.dialogs

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.behraz.fastermixer.batch.R
import kotlinx.android.synthetic.main.layout_gps_error_dialog.*

class GpsErrorDialog(
    context: Context,
    private val onDismiss: () -> Unit,
    private val onChangeProviderClicked: () -> Unit
) : MyBaseFullScreenDialog(
    context,
    R.style.my_alert_dialog,
    R.layout.layout_gps_error_dialog
) {

    override fun initViews() {
        setCancelable(false)

        frame_check_settings.setOnClickListener {
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            dismiss()
            onDismiss.invoke()
        }

        frameChangeGpsProvider.setOnClickListener {
            onChangeProviderClicked.invoke()
            dismiss()
            onDismiss.invoke()
        }
    }
}