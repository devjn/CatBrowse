package com.github.devjn.catbrowse.ui.view.home

import androidx.lifecycle.ViewModel
import com.github.devjn.catbrowse.Provider
import com.github.devjn.catbrowse.data.Cat
import io.reactivex.rxjava3.subjects.PublishSubject

class HomeViewModel : ViewModel() {

    val allCats = ArrayList<Cat>()
    val newCats = PublishSubject.create<List<Cat>>()!!

    private val service = Provider.service
    private val catDisposable = newCats.subscribe { allCats.addAll(it) }

    private var page = 0

    init {
        // initial load
        loadMore()
    }

    fun onFavoriteClick(cat: Cat) {
        Provider.appData.toggleFavorite(cat)
    }

    fun loadMore() {
        val call = service.search(page++)
        call.subscribe({ response: List<Cat> ->
            newCats.onNext(response)
        }, { e: Throwable -> e.printStackTrace() })
    }

    override fun onCleared() {
        super.onCleared()
        catDisposable.dispose()
    }


}