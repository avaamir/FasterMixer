package com.behraz.fastermixer.batch.models

import com.behraz.fastermixer.batch.models.enums.EquipmentState
import com.behraz.fastermixer.batch.models.enums.EquipmentType
import com.behraz.fastermixer.batch.utils.general.exhaustiveAsExpression

data class AdminEquipment(
    val id: String,
    val name: String,
    val carId: String,
    private val _state: Int
) {
    val type: EquipmentType
        get() = when {
            name.contains("میکسر") -> EquipmentType.Mixer
            name.contains("لودر") -> EquipmentType.Loader
            name.contains("پمپ") -> EquipmentType.Pomp
            else -> EquipmentType.Other
        }.exhaustiveAsExpression()

    val state get() = when(_state) {
        1 -> EquipmentState.Fixing
        2 -> EquipmentState.Off
        3 -> EquipmentState.Using
        else -> EquipmentState.Other
    }

}