package com.behraz.fastermixer.batch.ui.dialogs

import android.app.Activity
import android.view.View
import android.view.WindowManager
import com.behraz.fastermixer.batch.R
import kotlinx.android.synthetic.main.dialog_message.*


class PompMessageDialog(
    activity: Activity,
    themeResId: Int,
    private val interactions: Interactions
) :
    MyBaseFullScreenDialog(
        activity, themeResId,
        R.layout.dialog_message
    ) {


    override fun initViews() {
        btnLab.setOnClickListener {
            interactions.onLabClicked()
            dismiss()
        }
        btnMessage.setOnClickListener {
            interactions.onMessageClicked()
            dismiss()
        }
        btnRepair.setOnClickListener {
            interactions.onBrokenClicked()
            dismiss()
        }
        btnStop.setOnClickListener {
            interactions.onStopClicked()
            dismiss()
        }
    }

    interface Interactions {
        fun onMessageClicked()
        fun onStopClicked()
        fun onLabClicked()
        fun onBrokenClicked()
    }
}

