package com.behraz.fastermixer.batch.utils.general

import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.behraz.fastermixer.batch.R
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.SnackbarLayout


fun Activity.snack(
    message: String,
    onAction: (() -> Unit)? = null,
    onDismissed: (() -> Unit)? = null
): Snackbar {
    // Create the SnackBar
    val activityView =
        window.decorView.findViewById<View>(android.R.id.content) //?: findViewById<View>(android.R.id.content).rootView


    val snackBar = Snackbar.make(activityView, "", Snackbar.LENGTH_INDEFINITE)
    // Get the SnackBar's layout view
    val layout = snackBar.view as SnackbarLayout
    // Hide the text
    val textView =
        layout.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
    textView.visibility = View.INVISIBLE

    // Inflate our custom view
    val snackView: View =
        layoutInflater.inflate(R.layout.view_my_snackbar, findViewById(R.id.custom_snack_container))

    // Configure the view
    val tvMessage = snackView.findViewById<TextView>(R.id.snackbar_text)
    tvMessage.text = message

    val btnAction = snackView.findViewById<Button>(R.id.snackbar_action)
    btnAction.text = "تلاش مجدد"
    btnAction.setOnClickListener {
        //   onAction?.invoke()
        snackBar.dismiss()
    }
    //tvMessage.setTextColor(Color.WHITE)

    //If the view is not covering the whole snackBar layout, add this line
    layout.setPadding(0, 0, 0, 0)

    // Add the view to the SnackBar's layout
    layout.addView(snackView, 0)

    snackBar.animationMode = BaseTransientBottomBar.ANIMATION_MODE_SLIDE

    //snackBar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.toast_background))

    snackBar.addCallback(object : Snackbar.Callback() {
        override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
            onDismissed?.invoke()
            onAction?.invoke()
        }
    })

    snackBar.show()
    return snackBar
}


fun Fragment.snack(
    message: String,
    onAction: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    // Create the SnackBar
    activity?.run {
        snack(message, onAction, onDismiss)
    }
}


/*
fun showSnack() {
    val x = Snackbar.make(
        mBinding.root,
        com.behraz.fastermixer.batch.utils.fastermixer.Constants.SERVER_ERROR,
        Snackbar.LENGTH_INDEFINITE
    )
    x.setActionTextColor(Color.BLUE)
    x.setAction("تلاش مجدد") {

    }.show()

    val iranSans = ResourcesCompat.getFont(this, R.font.iransans)

    x.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        .setTypeface(iranSans)

    x.view.findViewById<Button>(com.google.android.material.R.id.snackbar_action)
        .setTypeface(iranSans)

    x.setAnimationMode(ANIMATION_MODE_SLIDE)
    x.show()
}
*/
