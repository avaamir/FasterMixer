package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.requests.behraz.GetReportRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo

class ReportViewModel : ViewModel() {

    val speed: Long = 1000L //3 ta addad bayad dashte bashe //slow, normal, fast
    var currentPointIndex = MutableLiveData<Int>()

    var isPaused = true
        set(value) {
            field = value
            _isPausedLiveData.value = value
        }
    private val _isPausedLiveData = MutableLiveData<Boolean>()
    val isPausedLiveData : LiveData<Boolean> = _isPausedLiveData


    private val getDrawRoadReportEvent = MutableLiveData<GetReportRequest>()
    val drawRoadReport = Transformations.switchMap(getDrawRoadReportEvent) {
        RemoteRepo.getDrawRoadReport(it)
    }

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

    fun getDrawRoadReport(getReportRequest: GetReportRequest) {
        getDrawRoadReportEvent.value = getReportRequest
    }

    fun tryGetDrawRoadAgain() {
        getDrawRoadReportEvent.value = getDrawRoadReportEvent.value
    }


    override fun onCleared() {
        super.onCleared()
    }

}