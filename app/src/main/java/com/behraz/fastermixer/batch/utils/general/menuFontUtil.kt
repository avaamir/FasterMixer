package com.behraz.fastermixer.batch.utils.general

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.style.TypefaceSpan
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import com.behraz.fastermixer.batch.R


fun Context.applyFontToMenu(menu: Menu) { //-1 means nothing has been selected
    for (i in 0 until menu.size()) {
        applyFontToMenuItem(menu.getItem(i), this)
    }
}

private fun applyFontToMenuItem(mi: MenuItem, mContext: Context) {
    if (mi.hasSubMenu()) {
        for (i in 0 until mi.subMenu.size()) {
            applyFontToMenuItem(mi.subMenu.getItem(i), mContext)
        }
    }

    val font = ResourcesCompat.getFont(mContext, R.font.iransans_light)!!
    mi.title = createSpannableString(mi.title, font, Color.BLACK)
}

fun createSpannableString(title: CharSequence, font: Typeface, textColor: Int) =
    SpannableString(title).apply {
        setSpan(
            CustomTypefaceSpan("", font, textColor),
            0,
            this.length,
            Spannable.SPAN_INCLUSIVE_INCLUSIVE
        )
    }

class CustomTypefaceSpan(
    family: String?, private val mTypeface: Typeface,
    @ColorInt val textColor: Int
) :
    TypefaceSpan(family) {



    override fun updateDrawState(ds: TextPaint) {
        ds.color = textColor
        applyCustomTypeFace(ds, mTypeface)
    }

    override fun updateMeasureState(paint: TextPaint) {
        applyCustomTypeFace(paint, mTypeface)
    }

    companion object {
        private fun applyCustomTypeFace(paint: Paint, tf: Typeface) {
            val oldStyle: Int
            val old: Typeface = paint.typeface
            oldStyle = old.style
            val fake = oldStyle and tf.style.inv()
            if (fake and Typeface.BOLD != 0) {
                paint.isFakeBoldText = true
            }
            if (fake and Typeface.ITALIC != 0) {
                paint.textSkewX = -0.25f
            }
            paint.typeface = tf
        }
    }
}


