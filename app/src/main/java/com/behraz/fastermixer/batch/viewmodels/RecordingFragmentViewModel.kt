package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.respository.RemoteRepo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class RecordingFragmentViewModel : ViewModel() {

    private val sendVoiceEvent = MutableLiveData<MultipartBody.Part>()

    val sendVoiceResponse = Transformations.switchMap(sendVoiceEvent) {
        RemoteRepo.sendVoice(it)
    }

    fun sendRecordFile(file: File) {
        sendVoiceEvent.value =  MultipartBody.Part.createFormData("record", file.name, file.asRequestBody("*/*".toMediaTypeOrNull()))
    }
}