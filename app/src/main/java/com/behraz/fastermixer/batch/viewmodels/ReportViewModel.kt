package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.requests.behraz.GetReportRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo

class ReportViewModel : ViewModel() {

    private val getFullReportEvent = MutableLiveData<GetReportRequest>()
    val fullReport = Transformations.switchMap(getFullReportEvent) {
        RemoteRepo.getFullReport(it)
    }

    fun getFullReport(getReportRequest: GetReportRequest) {
        getFullReportEvent.value = getReportRequest
    }

    fun tryAgain() {
        getFullReportEvent.value = getFullReportEvent.value
    }
}