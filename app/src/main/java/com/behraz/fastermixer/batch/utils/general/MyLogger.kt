package com.behraz.fastermixer.batch.utils.general

import com.behraz.fastermixer.batch.BuildConfig


inline fun Any.log(message: Any?, tag: String = "debug") {
    if (BuildConfig.DEBUG) {
        val enclosingClass = javaClass.enclosingClass

        val mSimpleName = enclosingClass?.name?.let { name ->
            val index = name.indexOfLast { ch ->
                ch == '.'
            }

            if (index != -1)
                name.substring(index)
            else
                name
        } ?: javaClass.name

        println(
            "$tag:${this::class.simpleName ?: mSimpleName} -> $message"
        )
    }
}