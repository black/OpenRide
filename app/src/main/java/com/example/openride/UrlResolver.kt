package com.example.openride

import okhttp3.OkHttpClient
import okhttp3.Request

object UrlResolver {

    fun resolve(url: String): String? {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request)
            .execute()
            .use { response ->
                return response.request.url.toString()
            }
    }
}