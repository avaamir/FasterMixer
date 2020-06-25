package com.behraz.fastermixer.batch.ui.dialogs

import android.content.Context
import com.behraz.fastermixer.batch.R
import kotlinx.android.synthetic.main.layout_location_permission_dialog.*

class LocationPermissionDialog(
    context: Context,
    themeResId: Int,
    private val onGranted: (Boolean, LocationPermissionDialog) -> Unit
) : MyBaseDialog(
    context, themeResId,
    R.layout.layout_location_permission_dialog
) {

    init {
        setCancelable(false)
    }

    override fun initViews() {
        btn_accept.setOnClickListener { onGranted(true, this) }
        btn_denied.setOnClickListener { onGranted(false, this) }
    }
}