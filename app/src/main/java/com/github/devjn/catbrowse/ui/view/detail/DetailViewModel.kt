package com.github.devjn.catbrowse.ui.view.detail

import androidx.lifecycle.ViewModel
import com.github.devjn.catbrowse.Provider
import com.github.devjn.catbrowse.data.Cat

class DetailViewModel : ViewModel() {
    fun onFavoriteClicked(cat: Cat) {
        Provider.appData.toggleFavorite(cat)
    }
}