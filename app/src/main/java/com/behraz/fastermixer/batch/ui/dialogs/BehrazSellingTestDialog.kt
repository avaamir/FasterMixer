package com.behraz.fastermixer.batch.ui.dialogs

import android.app.Activity
import android.view.View
import android.view.WindowManager
import com.behraz.fastermixer.batch.R
import kotlinx.android.synthetic.main.layout_behraz_selling_test.*


class BehrazSellingTestDialog(
    activity: Activity,
    themeResId: Int,
    private val interactions: Interactions
) :
    MyBaseDialog(
        activity, themeResId,
        R.layout.layout_behraz_selling_test
    ) {


    override fun initViews() {
        btnMessage.setOnClickListener {
            interactions.onTestDialogPompLogin()
            dismiss()
        }

        btnStop.setOnClickListener {
            interactions.onTestDialogBatchLogin()
            dismiss()
        }
    }


    override fun show() {
        window!!.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        )

        // Set full-sreen mode (immersive sticky):
        window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        super.show()


        // Set dialog focusable so we can avoid touching outside:
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    }

    interface Interactions {
        fun onTestDialogPompLogin()
        fun onTestDialogBatchLogin()
    }
}

