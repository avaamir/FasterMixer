package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.*
import com.behraz.fastermixer.batch.models.User
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.general.Event
import kotlin.concurrent.fixedRateTimer

class AdminPanelFragmentViewModel : ParentViewModel() {

    private var getPlansEvent = MutableLiveData(Event(Unit))
    val plans = Transformations.switchMap(getPlansEvent) {
        RemoteRepo.getAdminPlans()
    }

    val userAndCompanyName: String get() = UserConfigs.user.value?.run {
        "$name (بارز)" //TODO get company name from server
    } ?: "تعریف نشده"


    override fun onTimerTick(user: User) {
        getPlansEvent?.postValue(Event(Unit)) //chun timer dar kelas super call mishe inja hanuz init nashode khater hamin not null budan check mishe
    }

}