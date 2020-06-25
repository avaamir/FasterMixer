package com.behraz.fastermixer.batch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.behraz.fastermixer.batch.models.Mixer
import com.behraz.fastermixer.batch.utils.fastermixer.fakeMixers

class MixerListFragmentViewModel : ViewModel() {
    private val _mixers = MutableLiveData<List<Mixer>>()
    val mixers: LiveData<List<Mixer>> get() = _mixers


    init {
        _mixers.value =
            fakeMixers() //TODO ui test purpose
    }
}