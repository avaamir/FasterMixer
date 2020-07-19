package com.behraz.fastermixer.batch.utils.general

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.behraz.fastermixer.batch.models.requests.CircleFence
import org.osmdroid.util.GeoPoint
import java.util.concurrent.TimeUnit


fun millisToTimeString(millis: Long): String {
    var uptime = millis
    val days: Long = TimeUnit.MILLISECONDS.toDays(uptime)
    uptime -= TimeUnit.DAYS.toMillis(days)
    val hours: Long = TimeUnit.MILLISECONDS.toHours(uptime)
    uptime -= TimeUnit.HOURS.toMillis(hours)
    val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(uptime)
    uptime -= TimeUnit.MINUTES.toMillis(minutes)
    val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(uptime)

    return "${if (days < 10) "0$days" else days} : ${if (hours < 10) "0$hours" else hours} : ${if (minutes < 10) "0$minutes" else minutes} : ${if (seconds < 10) "0$seconds" else seconds}"
}

fun standardCartNumber(number: String): String {
    val sb = StringBuilder(number)
    for (i in 1..number.length) {
        if (i != number.length && i % 4 == 0) sb.insert(number.length - i, '-')
    }
    return sb.toString()
}

fun isNumeric(str: String): Boolean {
    return try {
        str.toDouble()
        true
    } catch (e: NumberFormatException) {
        false
    }
}

fun getBitmapFromVectorDrawable(
    context: Context,
    @DrawableRes drawableId: Int,
    width: Int = 80,
    height: Int = 80
): Bitmap {
    val drawable = ContextCompat.getDrawable(context, drawableId)!!
    //Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)
    drawable.draw(canvas)
    return bitmap
}

fun <T> convertToStringList(list: List<T>): List<String> {
    /**
     * T object should override 'toString()' method for your own specific use
     */
    val strList: MutableList<String> =
        ArrayList()
    for (t in list) {
        strList.add(t.toString())
    }
    return strList
}

fun getShareIntentUriViaWeb(actionName: String, bundle: Bundle): String {
    val intent =
        Intent(actionName) //todo for actionName you should use (packageName + something) to avoid conflict with other applications
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.addCategory(Intent.CATEGORY_BROWSABLE)
    intent.flags = Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT
    intent.putExtras(bundle)
    return intent.toUri(Intent.URI_INTENT_SCHEME)
}

fun setClipboard(context: Context, s: String?) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("invite_code", s)
    clipboard.setPrimaryClip(clip)
}