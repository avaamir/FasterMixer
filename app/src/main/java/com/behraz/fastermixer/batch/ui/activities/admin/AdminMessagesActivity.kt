package com.behraz.fastermixer.batch.ui.activities.admin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.utils.general.toast

class AdminMessagesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val ref = AdminActivity.reference?.get()
        if (ref == null) {
            toast("خطایی به وجود آمده است")
            finish()
        }
        setContentView(R.layout.admin_messages_activity)
    }
}