package com.behraz.fastermixer.batch.ui.customs.general

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.NumberPicker
import androidx.core.content.res.ResourcesCompat
import com.behraz.fastermixer.batch.R

class MyPicker : NumberPicker {
    private var typeface: Typeface? = null

    constructor(context: Context?) : super(context)
    constructor(
        context: Context?, attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(
        context: Context?, attrs: AttributeSet?, defStyleAttr: Int
    ) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    override fun addView(child: View) {
        super.addView(child)
        updateView(child)
    }

    override fun addView(
        child: View, index: Int,
        params: ViewGroup.LayoutParams
    ) {
        super.addView(child, index, params)
        updateView(child)
    }

    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        super.addView(child, params)
        updateView(child)
    }

    private fun updateView(view: View) {
        if (view is EditText) {
            view.typeface = typeface ?: ResourcesCompat.getFont(
                context,
                R.font.iransans
            ).also { typeface = it }
            //view.textSize = 25f
            view.setTextColor(Color.BLACK)
        }
    }
}