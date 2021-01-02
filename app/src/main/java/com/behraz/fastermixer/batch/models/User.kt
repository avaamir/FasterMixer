package com.behraz.fastermixer.batch.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.behraz.fastermixer.batch.models.enums.UserType
import com.behraz.fastermixer.batch.utils.general.getEnumById


@Entity(tableName = "user_tb")
data class User(
    @PrimaryKey
    val id: Int,
    val name: String?,
    val personalCode: String?,
    val token: String,
    val roleId: Int?,
    val equipmentId: Int?
)  {
    @Ignore
    val userType: UserType = getEnumById(UserType::roleId, roleId)
}