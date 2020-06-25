package com.behraz.fastermixer.batch.models

import java.lang.Exception

data class Progress(
    val id: Int,
    val name: String,
    private var _state: Int
) {
    var state
        get() = when (_state) {
            0 -> ProgressState.NotStarted
            1 -> ProgressState.InProgress
            2 -> ProgressState.Done
            else -> throw Exception("invalid state id")
        }
    set(value) {
        _state = value.id
    }


}


enum class ProgressState(val id: Int) {
    NotStarted(0),
    InProgress(1),
    Done(2)
}