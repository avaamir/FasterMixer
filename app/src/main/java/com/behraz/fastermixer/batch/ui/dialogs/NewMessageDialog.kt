package com.behraz.fastermixer.batch.ui.dialogs

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.widget.Button
import android.widget.TextView
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.utils.general.log


class NewMessageDialog(
    private val message: Message,
    context: Context,
    themeResId: Int = R.style.my_alert_dialog
) :
    MyBaseFullScreenDialog(context, themeResId, R.layout.new_message_dialog) {

    override fun initViews() {
        setCancelable(false)
        val tvTitle = findViewById<TextView>(R.id.textView7)
        val tvContent = findViewById<TextView>(R.id.textView5)
        val btnOk = findViewById<Button>(R.id.btnOk)

        tvTitle.text = message.senderName
        tvContent.text = message.content
        btnOk.setOnClickListener { dismiss() }

        //TODO make a notification in status bar
        try {
            val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val r = RingtoneManager.getRingtone(context, notification)
            r.play()
        } catch (e: Exception) {
            log(e.message)
        }
        //notifyAlert()
    }
}