package com.github.devjn.catbrowse.api

import com.github.devjn.catbrowse.data.Cat
import io.reactivex.rxjava3.core.Single
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CatApiService {

    @GET("images/search?format=json&limit=10&order=ASC&page=page&include_breeds=true&has_breeds=1")
    fun search(@Query("page") page: Int = 0): Single<List<Cat>>

    @GET("images/search?format=json&order=ASC&page=page&limit=10&include_breeds=true&has_breeds=1&breed_ids=breeds")
    fun search(
        @Query("page") page: Int = 0,
        @Query("breeds") breeds: String
    ): Single<List<Cat>>

    @Multipart
    @POST("images/upload")
    fun uploadCatImage(@Part file: MultipartBody.Part): Single<ResponseBody>

    @GET("images/{image_id}")
    fun getCatById(@Path("image_id") id: String): Single<Cat>

}
