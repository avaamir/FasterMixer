package com.behraz.fastermixer.batch.utils.general

import java.util.*


class FIFO<T : Any>(private val capacity: Int) : LinkedList<T>() {
    override fun add(element: T): Boolean {
        if (size == capacity)
            poll()
        return super.add(element)
    }

    override fun add(index: Int, element: T) {
        if (size == capacity)
            poll()
        super.add(index, element)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        if (size == capacity)
            poll()
        return super.addAll(elements)
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        if (size == capacity)
            poll()
        return super.addAll(index, elements)
    }

    override fun addFirst(e: T) {
        if (size == capacity)
            poll()
        super.addFirst(e)
    }

    override fun addLast(e: T) {
        if (size == capacity )
            poll()
        super.addLast(e)
    }


}