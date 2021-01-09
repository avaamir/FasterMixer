package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.requests.behraz.GetReportRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo

class ReportViewModel : ViewModel() {

    var request = GetReportRequest()


    private val getSummeryReportEvent = MutableLiveData<GetReportRequest>()
    val summeryReport = Transformations.switchMap(getSummeryReportEvent) {
        RemoteRepo.getSummeryReport(it.request)
    }

    private val getFullReportEvent = MutableLiveData<GetReportRequest>()
    val fullReport = Transformations.switchMap(getFullReportEvent) {
        RemoteRepo.getFullReport(it.request)
    }

    fun getFullReport() {
        getFullReportEvent.value = request
    }

    fun getSummeryReport() {
        getSummeryReportEvent.value = request
    }

    fun tryGetFullReportAgain() {
        getFullReportEvent.value = getFullReportEvent.value
    }

    fun tryGetSummeryReportAgain() {
        getSummeryReportEvent.value = getSummeryReportEvent.value
    }


}