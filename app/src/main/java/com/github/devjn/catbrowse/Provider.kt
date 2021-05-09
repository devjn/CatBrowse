package com.github.devjn.catbrowse

import com.github.devjn.catbrowse.api.RetrofitService

object Provider {

    val service by lazy { RetrofitService.catService() }

    val appData = AppData

}