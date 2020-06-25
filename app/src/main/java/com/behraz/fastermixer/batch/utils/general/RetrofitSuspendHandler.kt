package com.behraz.fastermixer.batch.utils.general

import kotlinx.coroutines.*
import java.io.IOException
import java.lang.reflect.UndeclaredThrowableException

/*
* todo Lunch Retrofit API with this wrapper and function until they fix the coroutine network timeout exception
* todo response may be null for hTTP 400 (BAD request) , use Call instead if its happened, this is an open issue right now for retrofit 2.6.0

* */

fun CoroutineScope.launchApi(
    onResponse: suspend CoroutineScope.() -> Unit,
    onFailure: ((Exception) -> Unit)? = null
): Job = launch {
    try {
        onResponse()
    } catch (ex: IOException) {
        if (onFailure == null) {
            println("debug: tryLaunch -> OnFailure: message ${ex.message}")
        } else {
            onFailure(ex)
        }
    } catch (ex: UndeclaredThrowableException) {
        if (onFailure == null) {
            println("debug: tryLaunch -> OnFailure: message ${ex.message}")
        } else {
            onFailure(ex)
        }
    }
}