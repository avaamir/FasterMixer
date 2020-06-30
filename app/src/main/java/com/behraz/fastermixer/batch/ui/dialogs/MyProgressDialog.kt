package com.behraz.fastermixer.batch.ui.dialogs

import android.content.Context
import com.behraz.fastermixer.batch.R

class MyProgressDialog(
    context: Context,
    themeResId: Int
) : MyBaseDialog(
    context, themeResId,
    R.layout.layout_my_progress_dialog
) {
    override fun initViews() {
        setCancelable(false)
    }
}