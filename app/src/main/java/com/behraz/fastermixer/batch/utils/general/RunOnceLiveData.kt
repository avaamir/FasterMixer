package com.behraz.fastermixer.batch.utils.general
import androidx.lifecycle.LiveData


abstract class RunOnceMutableLiveData<T> : RunOnceLiveData<T>() {

    public override fun setValue(value: T) {
        super.setValue(value)
    }

    public override fun postValue(value: T) {
        super.postValue(value)
    }
}


abstract class RunOnceLiveData<T> : LiveData<T>() {
    abstract fun onActiveRunOnce()

    private var isFirstTime = true
    override fun onActive() {
        if (isFirstTime) {
            isFirstTime = false
            onActiveRunOnce()
        }
    }

    fun activateAgain() {
        isFirstTime = true
    }
}