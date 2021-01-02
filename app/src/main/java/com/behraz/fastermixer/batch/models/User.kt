package com.behraz.fastermixer.batch.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.behraz.fastermixer.batch.models.enums.UserType
import com.behraz.fastermixer.batch.utils.general.getEnumById


@Entity(tableName = "user_tb")
data class User(
    @PrimaryKey
    var personId: Int,
    var name: String?,
    var token: String,
    var roleId: Int?,
    var equipmentId: Int?
)  {
    @Ignore
    val userType: UserType = getEnumById(UserType::roleId, roleId)
}