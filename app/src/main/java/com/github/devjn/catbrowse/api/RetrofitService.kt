package com.github.devjn.catbrowse.api

import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object RetrofitService {

    private const val CAT_BASE_URL = "https://api.thecatapi.com/v1/"

    private const val CACHE_CONTROL = "Cache-Control"

    private val okHttp: OkHttpClient

    init {
//        val httpCacheDirectory = File(App.appContext.cacheDir, "responses")
//        val cacheSize = 10L * 1024 * 1024 // 10 MiB
//        val cache = Cache(httpCacheDirectory, cacheSize)

        okHttp = OkHttpClient.Builder()
            .authenticator(Authenticator { route, response ->
                response.request().newBuilder()
                    .header("x-api-key", "9b7e282d-2a67-4c7b-a9fd-3f3e4056e949").build()
            })
            .addNetworkInterceptor(provideCacheInterceptor())
//            .cache(cache)
            .build()
    }

    fun catService() = builder
        .baseUrl(CAT_BASE_URL).build()
        .create(CatApiService::class.java)

    private val builder = Retrofit.Builder()
        .baseUrl(CAT_BASE_URL)
        .client(okHttp)
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())

    private fun provideCacheInterceptor(): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())

            // re-write response header to force use of cache
            val cacheControl = CacheControl.Builder()
                .maxAge(10, TimeUnit.MINUTES)
                .build()

            response.newBuilder()
                .header(CACHE_CONTROL, cacheControl.toString())
                .build()
        }
    }

}
