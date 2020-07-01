package com.behraz.fastermixer.batch.ui.dialogs

import android.content.Context
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.utils.general.millisToTimeString
import kotlinx.android.synthetic.main.layout_record_dialog.*
import kotlin.concurrent.fixedRateTimer

class RecordDialog(
    context: Context,
    themeResId: Int,
    private val onStopRecordClicked: () -> Unit
) : MyBaseDialog(
    context, themeResId,
    R.layout.layout_record_dialog
) {
    private var tick = 0

    override fun initViews() {
        btnRecord.setOnClickListener {
            onStopRecordClicked()
            dismiss()
        }

        fixedRateTimer(period = 1000L) {
            tick++
            tvTick.text = millisToTimeString(tick * 1000L).substring(10)
        }

    }
}