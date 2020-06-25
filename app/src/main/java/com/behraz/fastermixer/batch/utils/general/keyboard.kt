package com.behraz.fastermixer.batch.utils.general

import android.app.Activity
import android.content.Context
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