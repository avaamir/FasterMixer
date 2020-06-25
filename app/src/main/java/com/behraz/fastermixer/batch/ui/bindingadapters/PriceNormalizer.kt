package com.behraz.fastermixer.batch.ui.bindingadapters

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("price")
fun priceToStr(textView: TextView, price: Int) {
    textView.text = priceToStr(price)
    /*if(shouldStrike) textView.setPaintFlags(textView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);*/
}

@BindingAdapter(value = ["price"])
fun priceToStr(textView: TextView, price: String?) {
    price?.let {
        textView.text = priceToStr(it)
    }
}

fun priceToStr(price: String): String {
    val sb = StringBuilder(price)
    for (i in 1..price.length) {
        if (i != price.length && i % 3 == 0) sb.insert(price.length - i, ',')
    }
    return sb.toString()
}

fun priceToStr(price: Int): String {
    val s = price.toString()
    val sb = StringBuilder(s)
    for (i in 1..s.length) {
        if (i != s.length && i % 3 == 0) sb.insert(s.length - i, ',')
    }
    return sb.toString()
}




