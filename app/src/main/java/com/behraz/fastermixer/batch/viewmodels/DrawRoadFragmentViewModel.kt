package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.ReportPoint
import com.behraz.fastermixer.batch.models.requests.behraz.ApiResult
import com.behraz.fastermixer.batch.models.requests.behraz.GetReportRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo

class DrawRoadFragmentViewModel: ViewModel() {

    var request : GetReportRequest? = null


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

    fun getDrawRoadReport() {
        RemoteRepo.getDrawRoadReport(request!!.drawRoadRequest) {
            _drawRoadReport.value = it
        }
    }

    fun tryGetDrawRoadAgain() {
        getDrawRoadReport()
    }




}