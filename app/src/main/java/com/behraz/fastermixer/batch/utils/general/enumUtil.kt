package com.behraz.fastermixer.batch.utils.general

import kotlin.reflect.KProperty1

inline fun <reified T : Enum<T>, K> getEnumById(compareProperty: KProperty1<T, K>, id: K): T {
    val values = enumValues<T>()
    for (v in values) {
        if (compareProperty.get(v) == id)
            return v
    }
    throw Exception("id $id does not exist in enum type ${T::class.simpleName}")
}