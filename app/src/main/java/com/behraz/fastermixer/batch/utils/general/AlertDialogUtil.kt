package com.behraz.fastermixer.batch.utils.general

import android.app.Activity
import android.graphics.Color
import android.util.TypedValue
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.app.FasterMixerApplication


fun Activity.alert(
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    isCancelable: Boolean = true,
    onNegativeClicked: (() -> Unit)? = null,
    onPositiveClicked: () -> Unit
) {
    val typeFace = (application as FasterMixerApplication).iransansLight

    val dialog = AlertDialog.Builder(this)
        .setCancelable(isCancelable)
        .setMessage(message)
        .setCustomTitle(TextView(this).apply {
            text = title
            typeface = (application as FasterMixerApplication).iransans
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)

            val px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 16f, resources.displayMetrics
            ).toInt()
            /*val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(px, px, px, px)
            layoutParams = params*/
            setTextColor(Color.BLACK)
            setPadding(0, px, px, 0)
        })


        .setPositiveButton(positiveButtonText) { _, _ ->
            onPositiveClicked.invoke()
        }
        .setNegativeButton(negativeButtonText) { _, _ ->
            onNegativeClicked?.invoke()
        }.create()

    dialog.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)
    dialog.show()
    dialog.window?.decorView?.systemUiVisibility = window.decorView.systemUiVisibility
    //Clear the not focusable flag from the window
    dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE)


    val tvMessage = dialog.findViewById<TextView>(android.R.id.message)
    val btn1 = dialog.findViewById<Button>(android.R.id.button1)
    val btn2 = dialog.findViewById<Button>(android.R.id.button2)

    tvMessage?.typeface = typeFace
    btn1?.typeface = typeFace
    btn2?.typeface = typeFace
}

fun Fragment.alert(
    title: String,
    message: String,
    positiveButtonText: String,
    negativeButtonText: String,
    isCancelable: Boolean = true,
    onNegativeClicked: (() -> Unit)? = null,
    onPositiveClicked: () -> Unit
) {
    activity?.alert(
        title,
        message,
        positiveButtonText,
        negativeButtonText,
        isCancelable,
        onNegativeClicked,
        onPositiveClicked
    )
}