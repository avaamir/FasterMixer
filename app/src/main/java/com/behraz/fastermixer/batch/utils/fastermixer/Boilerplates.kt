package com.behraz.fastermixer.batch.utils.fastermixer

import android.app.Activity
import com.behraz.fastermixer.batch.utils.general.alert


fun Activity.logoutAlertMessage(onPositiveClicked: () -> (Unit)) {
    alert(
        title = "خروج از حساب",
        message = "آیا از خروج اطمینان دارید؟",
        positiveButtonText = "بله، خارج میشوم",
        negativeButtonText = "انصراف",
        onPositiveClicked = onPositiveClicked
    )
}
