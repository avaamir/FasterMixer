package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.AdminEquipment
import com.behraz.fastermixer.batch.models.Plan
import com.behraz.fastermixer.batch.models.User
import com.behraz.fastermixer.batch.models.enums.PlanType.*
import com.behraz.fastermixer.batch.models.enums.ReportType
import com.behraz.fastermixer.batch.models.requests.behraz.ApiResult
import com.behraz.fastermixer.batch.models.requests.behraz.succeedRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.utils.general.DoubleTrigger
import com.behraz.fastermixer.batch.utils.general.Event
import com.behraz.fastermixer.batch.utils.general.exhaustiveAsExpression

class AdminActivityViewModel : EquipmentViewModel() {
    //UI Events
    var eventReportEquipment: Event<Pair<AdminEquipment, ReportType>>? = null

    val eventOnVehicleSelectedToShowOnMap = MutableLiveData<Event<AdminEquipment>>()
    val eventOnRouteToCarClicked = MutableLiveData<Event<AdminEquipment>>()
    val eventOnShowEquipmentsDetails = MutableLiveData<Event<Unit>>()


    private val getAdminAccountPageEvent = MutableLiveData<Unit>()


    val adminAccountPageResponse = Transformations.switchMap(getAdminAccountPageEvent) {
        RemoteRepo.getAdminAccountPageData()
    }

    private var isSortByState = true

    private val getMessagesEvent = MutableLiveData(Event(Unit))
    override val messages = Transformations.switchMap(getMessagesEvent) {
        RemoteRepo.getAdminMessages().map {
            if (it.isSucceed)
                it.entity!!
            else
                ArrayList()
        }
    }

    var planType = Today
        set(value) {
            field = value
            getPlansEvent.value = Event(Unit)
        }

    private var pastRequests: ApiResult<List<Plan>>? = null
    private val getPlansEvent = MutableLiveData(Event(Unit))
    val plans = Transformations.switchMap(getPlansEvent) {
        when (planType) {
            Today -> RemoteRepo.getTodayPlansForAdmin()
            All -> RemoteRepo.getAllPlansForAdmin()
            Future -> RemoteRepo.getFuturePlansForAdmin()
            Past -> {
                //TODO chun darkhast haye gozashte emkan nadarad dar tool ke baname baz hast avaz beshavad (magar in ke az saat 12 shab rad beshavad va vared rooze jadid shavim) yek bar ke req zadim dar memory zakhire mikonim va harbar az haman cache estefade mikonim
                if (pastRequests != null) MutableLiveData(pastRequests!!) else RemoteRepo.getPastPlansForAdmin()
                    .map {
                        if (it.isSucceed) {
                            pastRequests = it
                        }
                        it
                    }
            }
            NotEnded -> RemoteRepo.getNotFinishedPlansForAdmin()
        }.exhaustiveAsExpression()
    }

    private val getEquipmentsEvent = MutableLiveData(Event(Unit))
    private val sortEquipmentsEvent = MutableLiveData<List<AdminEquipment>>()
    val equipments =
        Transformations.switchMap(DoubleTrigger(getEquipmentsEvent, sortEquipmentsEvent)) {
            if (it.first?.getEventIfNotHandled() != null) {
                RemoteRepo.getEquipmentsForAdmin().map { response ->
                    if (response?.entity != null) {
                        response.copy(
                            entity = sortEquipments(response.entity, isSortByState)
                        )
                    } else {
                        response
                    }
                }
            } else {
                MutableLiveData(succeedRequest(sortEquipments(it.second!!, isSortByState)))
            }
        }

    fun sortEquipments(byState: Boolean): Boolean {
        equipments.value?.entity?.let {
            isSortByState = byState
            sortEquipmentsEvent.value = it
            return true
        }
        return false
    }

    private fun sortEquipments(
        equipments: List<AdminEquipment>,
        byState: Boolean
    ): List<AdminEquipment> {
        isSortByState = byState
        return equipments.sortedWith(if (byState) compareBy { it.state } else compareBy { it.type })
    }


    val userAndCompanyName: String
        get() = UserConfigs.user.value?.run {
            "$name (بارز)" //TODO get company name from server
        } ?: "تعریف نشده"


    override fun onTimerTick(user: User) {
        getMessagesEvent?.postValue(Event(Unit)) //chun timer dar kelas super call mishe inja hanuz init nashode khater hamin not null budan check mishe
        getPlansEvent?.postValue(Event(Unit)) //chun timer dar kelas super call mishe inja hanuz init nashode khater hamin not null budan check mishe
        getEquipmentsEvent?.postValue(Event(Unit))
    }

    fun getAdminAccountPage() {
        getAdminAccountPageEvent.value = Unit
    }

    fun getEquipments() {
        getEquipmentsEvent.value = Event(Unit)
    }

}