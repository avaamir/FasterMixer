package com.behraz.fastermixer.batch.respository.persistance.typeconverter

import androidx.room.TypeConverter
import com.behraz.fastermixer.batch.models.Phone
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PhoneListConverter {
    @TypeConverter
    fun fromPhoneList(phones: List<Phone?>?): String {
        val gson = Gson()
        val type = object : TypeToken<List<Phone>>() {}.type
        return gson.toJson(phones, type)
    }

    @TypeConverter
    fun toPhoneList(phonesJson: String): List<Phone?>? {
        val gson = Gson()
        val type = object: TypeToken<List<Phone>>() {}.type
        return gson.fromJson(phonesJson, type)
    }
}