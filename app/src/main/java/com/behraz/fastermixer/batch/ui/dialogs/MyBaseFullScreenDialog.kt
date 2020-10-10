package com.behraz.fastermixer.batch.ui.dialogs

import android.content.Context
import android.view.View
import android.view.WindowManager

abstract class MyBaseFullScreenDialog(context: Context, themeResId: Int, layoutResId: Int) : MyBaseDialog(context, themeResId, layoutResId) {



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



}