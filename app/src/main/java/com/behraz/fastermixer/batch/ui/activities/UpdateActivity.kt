package com.behraz.fastermixer.batch.ui.activities

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.behraz.fastermixer.batch.utils.general.AppUpdater
import com.behraz.fastermixer.batch.R
import com.behraz.fastermixer.batch.models.requests.behraz.UpdateResponse
import com.behraz.fastermixer.batch.ui.dialogs.MyProgressDialog
import com.behraz.fastermixer.batch.utils.fastermixer.Constants
import com.behraz.fastermixer.batch.utils.general.alert
import com.behraz.fastermixer.batch.utils.general.toast
import kotlinx.android.synthetic.main.activity_update.*

class UpdateActivity : AppCompatActivity(), AppUpdater.Interactions {

    private lateinit var dialog: MyProgressDialog

    private var isDownloadUpdateFinished = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            updateActivityRootFrame.post {
                intent.getParcelableExtra<UpdateResponse>(Constants.INTENT_UPDATE_ACTIVITY_UPDATE_RESPONSE)!!
                    .let {
                        alert(
                            "بروزرسانی",
                            "نسخه جدیدی از برنامه موجود میباشد. برای ادامه کار نیاز به بروزرسانی دارید. آیا ادامه میدهید؟",
                            "ادامه",
                            "خروج",
                            false,
                            { finish() }
                        ) {
                            AppUpdater(
                                this,
                                it.link,
                                "$cacheDir/FasterMixer.apk",
                                it.version,
                                this
                            ).startIfNeeded()
                        }
                    }

            }
        }

    }

    override fun onResume() {
        super.onResume()
        if (isDownloadUpdateFinished) {
            toast("باید بروزرسانی کنید")
            finish()
        }
    }


    override fun onDownloadStarted() {
        dialog = MyProgressDialog(this, R.style.my_alert_dialog, true)
        dialog.show()
    }

    override fun onProgressUpdate(progress: Int) {
        dialog.setProgress(progress)
        if (progress == 100) {
            isDownloadUpdateFinished = true
            dialog.dismiss()
        }
    }

    override fun onDownloadCancelled(message: String) {
        dialog.dismiss()
        toast(message, true)
    }
}