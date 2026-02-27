package com.nxim.pathfind.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private var instance: PathFindApi? = null
    var serverUrl: String = ""
        private set
    var apiToken: String = ""
        private set

    fun configure(url: String, token: String) {
        var normalizedUrl = url.trim()
        if (!normalizedUrl.startsWith("http://") && !normalizedUrl.startsWith("https://")) {
            normalizedUrl = "https://$normalizedUrl"
        }
        if (!normalizedUrl.endsWith("/")) {
            normalizedUrl += "/"
        }
        
        serverUrl = normalizedUrl
        apiToken = token

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer $apiToken")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            // Temporarily comment out logging in prod, or keep it conditionally
            // .addInterceptor(logging)
            .build()
            
        instance = Retrofit.Builder()
            .baseUrl(normalizedUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PathFindApi::class.java)
    }

    val api: PathFindApi
        get() = instance ?: throw IllegalStateException("ApiClient is not configured yet. Missing server credentials.")
        
    /**
     * Recreates a temporary API instance just for testing login connection.
     */
    fun createTestClient(url: String, token: String): PathFindApi {
        var normalizedUrl = url.trim()
        if (!normalizedUrl.startsWith("http://") && !normalizedUrl.startsWith("https://")) {
            normalizedUrl = "https://$normalizedUrl"
        }
        if (!normalizedUrl.endsWith("/")) {
            normalizedUrl += "/"
        }
        val client = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${token.trim()}")
                    .addHeader("Content-Type", "application/json")
                    .build()
                chain.proceed(request)
            }
            .build()
            
        return Retrofit.Builder()
            .baseUrl(normalizedUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PathFindApi::class.java)
    }
}
