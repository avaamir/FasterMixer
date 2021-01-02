package com.behraz.fastermixer.batch.utils.general

import com.behraz.fastermixer.batch.BuildConfig


inline fun Any.log(message: Any?, tag: String = "debug") {
    if (BuildConfig.DEBUG) println("$tag:${this::class.simpleName} -> $message")
}