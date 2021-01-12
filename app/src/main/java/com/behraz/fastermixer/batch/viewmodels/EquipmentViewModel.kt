package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.Message
import com.behraz.fastermixer.batch.models.User
import com.behraz.fastermixer.batch.models.requests.BreakdownRequest
import com.behraz.fastermixer.batch.models.requests.behraz.ApiResult
import com.behraz.fastermixer.batch.models.requests.behraz.ErrorType
import com.behraz.fastermixer.batch.respository.RemoteRepo
import com.behraz.fastermixer.batch.respository.UserConfigs
import com.behraz.fastermixer.batch.respository.persistance.messagedb.MessageRepo
import com.behraz.fastermixer.batch.utils.general.Event
import java.util.*
import kotlin.concurrent.fixedRateTimer

abstract class TimerViewModel : ViewModel() {

    protected abstract fun onTimerTick(user: User)

    val user get() = UserConfigs.user

    private val logOutEvent = MutableLiveData<Event<Unit>>()
    val logoutResponse = Transformations.switchMap(logOutEvent) {
        RemoteRepo.logout()
    }


    private var breakdownEvent = MutableLiveData<BreakdownRequest>()
    val breakdownResponse = Transformations.switchMap(breakdownEvent) {
        RemoteRepo.insertBreakdownRequest(it).map { response ->
            ApiResult(it, response.isSucceed, response.message, ErrorType.OK)
        }
    }

    private val timer: Timer

    init {
        timer = fixedRateTimer(period = 20000L) {
            user.value?.let { user ->  //TODO albate bazam momkene unauthorized bede chun shyad moghe check kardan login bashe ama bad if logout etefagh biofte, AMA jelo exception ro migire
                onTimerTick(user)
            }
        }
    }

    fun insertBreakdown(breakdownRequest: BreakdownRequest) {
        breakdownEvent.value = breakdownRequest
    }

    fun logout() {
        logOutEvent.value = Event(Unit)
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
        timer.purge()
    }
}


open class EquipmentViewModel : TimerViewModel() {
    @Volatile
    private var isGetMessageRequestActive = false
    val newMessage = MutableLiveData<Event<Message>>()
    open val messages = MessageRepo.allMessage

    override fun onTimerTick(user: User) {
        getMessages()
    }

    private fun getMessages() {
        if (!isGetMessageRequestActive) {
            isGetMessageRequestActive = true
            RemoteRepo.getMessage {
                isGetMessageRequestActive = false
                if (it != null) { //halat hayee ke khata vojud darad mohem ast, data az MessageRepo khande mishavad
                    if (!it.isSucceed) {
                        //TODO ???
                    } else {
                        it.entity?.let { messages ->
                            if (messages.isNotEmpty()) {
                                val lastMessage = messages[0]
                                newMessage.value = Event(lastMessage)
                            }
                        }
                    }
                } else {
                    //TODO
                }
            }
        }
    }

    fun seenMessage(message: Message) {
        MessageRepo.seenMessage(message)
    }

    fun seenAllMessages() {
        MessageRepo.seenAllMessages()
    }

}