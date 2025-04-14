package com.salir.paging

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PaginatedList<T> internal constructor(
    private val paging: Paging<T>,
    private val data: List<T>,
    private val coroutineScope: CoroutineScope,
) : Iterable<T> {

    private var used: Boolean = false

    init {
        if (data.isEmpty()) {
            used = true
            coroutineScope.launch { paging.loadNextPage() }
        }
    }

    override fun iterator(): Iterator<T> = PaginatedListIterator()

    inner class PaginatedListIterator internal constructor() : Iterator<T> {
        private var currentIndex: Int = 0

        override fun hasNext(): Boolean {
            val has = currentIndex != data.size - 1 && data.isNotEmpty()

            if (has && !used) {
                used = true
                coroutineScope.launch { paging.loadNextPage() }
            }

            return has
        }

        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException("No more items")

            val lastIndex = currentIndex
            currentIndex += 1

            return data[lastIndex]
        }
    }
}