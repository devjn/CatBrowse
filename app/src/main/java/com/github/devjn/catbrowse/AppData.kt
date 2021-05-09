package com.github.devjn.catbrowse

import com.github.devjn.catbrowse.data.Cat

/**
 * There should some database I/O, but in this case I just replicated a behaviour
 */
object AppData {

    private val listeners = ArrayList<OnDataChange>()
    private val favoriteCats = ArrayList<Cat>()

    fun getAllFavoriteCats() = favoriteCats.toMutableList() // return a copy

    fun toggleFavorite(cat: Cat) {
        if (!favoriteCats.remove(cat)) {
            cat.isFavorite = true
            favoriteCats.add(cat)
        } else {
            cat.isFavorite = false
        }
        notifyListeners()
    }

    fun notifyListeners() {
        // We create a copy of data to replicate database i/o
        listeners.forEach { it.onNewData(getAllFavoriteCats()) }
    }

    fun addListener(listener : OnDataChange) = listeners.add(listener)
    fun removeListener(listener: OnDataChange) = listeners.remove(listener)

    fun interface OnDataChange {
        fun onNewData(data : List<Cat>)
    }

}
