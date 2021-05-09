package com.github.devjn.catbrowse.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cat(
    val id: String,
    val url: String,
    val breeds: List<Breed>,
    var isFavorite: Boolean = false
) : Parcelable

@Parcelize
data class Breed(
    val id: String,
    val name: String,
    val description : String,
) : Parcelable