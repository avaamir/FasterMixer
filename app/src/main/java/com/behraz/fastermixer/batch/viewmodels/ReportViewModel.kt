package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.requests.behraz.GetReportRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo

class ReportViewModel : ViewModel() {
    private val getSummeryReportEvent = MutableLiveData<GetReportRequest>()
    val summeryReport = Transformations.switchMap(getSummeryReportEvent) {
        RemoteRepo.getSummeryReport(it)
    }

    private val getFullReportEvent = MutableLiveData<GetReportRequest>()
    val fullReport = Transformations.switchMap(getFullReportEvent) {
        RemoteRepo.getFullReport(it)
    }

    fun getFullReport(getReportRequest: GetReportRequest) {
        getFullReportEvent.value = getReportRequest
    }

    fun getSummeryReport(getReportRequest: GetReportRequest) {
       getSummeryReportEvent.value = getReportRequest
    }

    fun tryGetFullReportAgain() {
        getFullReportEvent.value = getFullReportEvent.value
    }

    fun tryGetSummeryReportAgain() {
        getSummeryReportEvent.value = getSummeryReportEvent.value
    }

}