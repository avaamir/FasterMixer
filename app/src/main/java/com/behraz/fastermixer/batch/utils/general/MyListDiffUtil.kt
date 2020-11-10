package com.behraz.fastermixer.batch.utils.general

import kotlin.reflect.KProperty1


/*when you have to list, and one of them is sourceList and another is newValues for example came from server
* and you need to newValues set in the source list*/

fun <K, V, T> HashMap<K, V>.diffSourceFromNewValues(
    newValues: Collection<T>?,
    compareProperty: KProperty1<T, K>,
    onSourceMapChange: OnSourceMapChange<K, V, T>
) {
    val excludeList = ArrayList(this.keys)
    newValues?.forEach { item ->
        val keyId = compareProperty.get(item)
        val itemInSource = this[keyId]
        if (itemInSource != null) { // if Contains
            excludeList.remove(keyId)
            onSourceMapChange.onItemExistInBoth(keyId, itemInSource, item)
        } else {
            val value = onSourceMapChange.onAddItem(keyId, item)
            this[keyId] = value
        }
    }
    excludeList.forEach { key ->
        onSourceMapChange.onRemoveItem(key)
        this.remove(key)
    }
}

/*
fun <Key, Value> HashMap<Key, Value>.diffSourceFromNewValues(
    newValues: Collection<Key>, onSourceListChange: OnSourceMapChange<Key, Value>
) {
    val excludeList = ArrayList(this.keys)
    newValues.forEach { key ->
        if (excludeList.contains(key)) {
            excludeList.remove(key)
        } else {
            val value = onSourceListChange.onAddItem(key)
            this[key] = value
        }
    }
    excludeList.forEach { key ->
        onSourceListChange.onRemoveItem(key)
        this.remove(key)
    }
}
*/


/*
fun <T> ArrayList<T>.diffSourceFromNewValues(
    newValues: Collection<T>, onSourceListChange: OnSourceListChange<T>
) {
    val excludeList = ArrayList(this)
    newValues.forEach {
        if (excludeList.contains(it)) {
            excludeList.remove(it)
        } else {
            this.add(it)
            onSourceListChange.onAddItem(it)
        }
    }
    excludeList.forEach {
        onSourceListChange.onRemoveItem(it)
        this.remove(it)
    }
}

interface OnSourceListChange<T> {
    fun onAddItem(item: T)
    fun onRemoveItem(item: T)
}
*/

interface OnSourceMapChange<K, V, T> { //T is for new values list
    fun onAddItem(key: K, item: T): V
    fun onItemExistInBoth(
        keyId: K,
        value: V,
        item: T
    ) //Check equality if needed, if newValue are different then update the source item

    fun onRemoveItem(keyId: K)
}