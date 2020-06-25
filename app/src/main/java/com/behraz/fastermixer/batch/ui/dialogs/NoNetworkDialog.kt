package com.behraz.fastermixer.batch.ui.dialogs

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.behraz.fastermixer.batch.R
import kotlinx.android.synthetic.main.layout_no_network_dialog.*

class NoNetworkDialog(
    context: Context,
    themeResId: Int
) : MyBaseDialog(
    context, themeResId,
    R.layout.layout_no_network_dialog
) {
    override fun initViews() {
        frame_check_settings.setOnClickListener{
            context.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
            dismiss()
        }
    }
}