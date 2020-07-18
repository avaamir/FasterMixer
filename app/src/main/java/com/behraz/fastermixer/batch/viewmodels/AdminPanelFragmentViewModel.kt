package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.*
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.general.Event
import kotlin.concurrent.fixedRateTimer

class AdminPanelFragmentViewModel : ViewModel() {


    val userAndCompanyName: String get() = UserConfigs.user.value?.run {
        "$name (بارز)" //TODO get company name from server
    } ?: "تعریف نشده"
    private val timer = fixedRateTimer(period = 10000L) {
        getPlansEvent.postValue(Event(Unit))
    }


    private var getPlansEvent = MutableLiveData(Event(Unit))

    val plans = Transformations.switchMap(getPlansEvent) {
        RemoteRepo.getAdminPlans()
    }


    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        timer.purge()
    }
}