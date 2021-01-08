package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.ReportPoint
import com.behraz.fastermixer.batch.models.requests.behraz.ApiResult
import com.behraz.fastermixer.batch.models.requests.behraz.GetReportRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo

class ReportViewModel : ViewModel() {

    var request = GetReportRequest()


    val speed: Long = 1000L //3 ta addad bayad dashte bashe //slow, normal, fast
    var currentPointIndex = MutableLiveData<Int>()

    var isPaused = true
        set(value) {
            field = value
            _isPausedLiveData.value = value
        }
    private val _isPausedLiveData = MutableLiveData<Boolean>()
    val isPausedLiveData: LiveData<Boolean> = _isPausedLiveData


    private var _drawRoadReport = MutableLiveData<ApiResult<List<ReportPoint>>>()
    val drawRoadReport  = Transformations.switchMap(_drawRoadReport) {
        if (it != null) {
            MutableLiveData(it)
        } else {
            MutableLiveData()
        }
    }

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

    fun getDrawRoadReport() {
        RemoteRepo.getDrawRoadReport(request.request) {
            _drawRoadReport.value = it
        }
    }

    fun tryGetDrawRoadAgain() {
        getDrawRoadReport()
    }

    fun clear() {
        _drawRoadReport.value = null
    }

    override fun onCleared() {
        super.onCleared()
        _drawRoadReport.value = null
    }

}