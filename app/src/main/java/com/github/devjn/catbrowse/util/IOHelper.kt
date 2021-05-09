package com.github.devjn.catbrowse.util

import android.content.ContentResolver
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody

object IOHelper {

    fun getMultipartBodyFromUri(contentResolver: ContentResolver, uri: Uri): MultipartBody.Part? {
        val byteArray: ByteArray?
        contentResolver.openInputStream(uri).use {
            byteArray = it?.readBytes()
        }
        if (byteArray == null) {
            return null
        }
        val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), byteArray)
        return MultipartBody.Part.createFormData("file", "cat", requestFile)
    }

}