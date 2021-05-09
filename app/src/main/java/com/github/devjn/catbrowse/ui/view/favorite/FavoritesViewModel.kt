package com.github.devjn.catbrowse.ui.view.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.devjn.catbrowse.AppData
import com.github.devjn.catbrowse.Provider
import com.github.devjn.catbrowse.data.Cat

class FavoritesViewModel : ViewModel() {
    private val appData = Provider.appData
    private val onDataChange = AppData.OnDataChange { data -> cats.value = data }

    val cats = MutableLiveData<List<Cat>>(appData.getAllFavoriteCats())

    init {
        appData.addListener(onDataChange)
    }

    fun onFavoriteClick(cat: Cat) {
        appData.toggleFavorite(cat)
    }

    override fun onCleared() {
        super.onCleared()
        appData.removeListener(onDataChange)
    }

}
