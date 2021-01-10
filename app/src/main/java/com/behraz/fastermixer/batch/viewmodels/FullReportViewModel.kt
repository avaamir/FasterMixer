package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.behraz.fastermixer.batch.models.FullReport
import com.behraz.fastermixer.batch.models.requests.behraz.GetReportRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo

class FullReportViewModel : ViewModel() {

    private companion object {
        const val CHUNK_SIZE = 20
    }

    private var page = 0
    var isPageEnd = false
        private set
    private var hasActiveRequest = false
    private val data = ArrayList<FullReport>()

    var request: GetReportRequest? = null


    private val getFullReportEvent = MutableLiveData<GetReportRequest.FullReportRequest>()
    val fullReport = Transformations.switchMap(getFullReportEvent) { request ->
        RemoteRepo.getFullReport(request).map {
            if (it.isSucceed) {
                val recData = it.entity!!
                if (recData.size < CHUNK_SIZE) {
                    isPageEnd = true
                } else {
                    page++
                }
                hasActiveRequest = false
                data.addAll(recData)
            } else {
                page--
            }
            it.copy(entity = data)
        }
    }


    fun nextPage() {
        if (!hasActiveRequest && !isPageEnd) {
            hasActiveRequest = true
            getFullReportEvent.value = request!!.fullReportRequest(page)
        }
    }

    fun tryGetFullReportAgain() {
        nextPage()
    }
}