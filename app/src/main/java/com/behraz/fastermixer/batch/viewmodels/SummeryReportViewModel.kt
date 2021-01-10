package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.requests.behraz.GetReportRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo

class SummeryReportViewModel : ViewModel() {
    var request: GetReportRequest? = null

    private val getSummeryReportEvent = MutableLiveData<GetReportRequest.SummeryReportRequest>()
    val summeryReport = Transformations.switchMap(getSummeryReportEvent) {
        RemoteRepo.getSummeryReport(it)
    }

    fun getSummeryReport() {
        getSummeryReportEvent.value = request!!.summeryReportRequest
    }

    fun tryGetSummeryReportAgain() {
        getSummeryReportEvent.value = getSummeryReportEvent.value
    }
}