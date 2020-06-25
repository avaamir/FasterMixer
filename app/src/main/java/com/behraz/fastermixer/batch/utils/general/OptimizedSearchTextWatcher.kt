package com.behraz.fastermixer.batch.utils.general

import android.text.Editable
import android.text.TextWatcher
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main

/*
* with this class after user typing ends wait for 500ms(default value) and then call itsSearchTime
* use cases: server search requests, address search requests
* */

abstract class OptimizedSearchTextWatcher(private val timeWaitForSearch: Long = 500) : TextWatcher {
    private lateinit var job: CompletableJob
    private var afterTime = 0L
    private val tick = if (timeWaitForSearch >= 500) 100 else timeWaitForSearch / 2
    private var isFirstTimeReach = true


    abstract fun itsSearchTime(s: Editable)


    override fun afterTextChanged(s: Editable) {
        if (s.length > 2) {
            afterTime = System.currentTimeMillis()

            if (!::job.isInitialized || !job.isActive) {
                job = Job()
                CoroutineScope(Main + job).launch {
                    while (true) {
                        delay(tick)
                        if (s.length == 4 && isFirstTimeReach) { // bar aval ke tedad harfha 4 shod ye req bezanim
                            isFirstTimeReach = false
                            itsSearchTime(s)
                        }
                        if (System.currentTimeMillis() - afterTime > timeWaitForSearch) {
                            itsSearchTime(s)
                            isFirstTimeReach = true
                            break
                        }
                    }
                    job.complete()
                }
            }
        }

    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}