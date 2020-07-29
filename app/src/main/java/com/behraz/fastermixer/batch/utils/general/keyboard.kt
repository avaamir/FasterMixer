package com.behraz.fastermixer.batch.utils.general

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.text.InputType
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

fun Activity.hideSoftKeyboard() {
    // Check if no view has focus:
    val view = this.currentFocus
    view?.let { v ->
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(v.windowToken, 0)
        v.clearFocus()
    }
}

fun Activity.showSoftKeyboard(editText: EditText) {
    this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as (InputMethodManager)
    imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Disable soft keyboard from appearing, use in conjunction with android:windowSoftInputMode="stateAlwaysHidden|adjustNothing"
 * @param editText
 */
fun EditText.disableKeyboardFromAppearing() {
    if (Build.VERSION.SDK_INT >= 21) {
        showSoftInputOnFocus = false
    } else {
        setRawInputType(InputType.TYPE_CLASS_TEXT)
        setTextIsSelectable(true)
    }
}

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
fun EditText.enableKeyboardAppearing() {
    showSoftInputOnFocus = true
}