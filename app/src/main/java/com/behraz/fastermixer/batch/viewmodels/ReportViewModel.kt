package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.requests.behraz.GetReportRequest
import com.behraz.fastermixer.batch.respository.RemoteRepo

class ReportViewModel : ViewModel() {
    var request = GetReportRequest()
}