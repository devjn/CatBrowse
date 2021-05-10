package com.github.devjn.catbrowse.util

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

object IOHelper {

    fun multipartBodyFromUri(contentResolver: ContentResolver, uri: Uri): MultipartBody.Part? {
        val byteArray = contentResolver.openInputStream(uri).use { it?.readBytes() } ?: return null
        val body = RequestBody.create(MediaType.get("image/*"), byteArray)
        return MultipartBody.Part.createFormData("file", null, body).also {
            println("body=${it.body()}, headers=${it.headers()}")
        }
    }

}