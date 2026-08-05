package com.siputzx.app.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET
    suspend fun callGet(@Url url: String, @QueryMap params: Map<String, String>): okhttp3.ResponseBody
}

object RetrofitClient {
    // API sebenarnya di api.siputzx.my.id — bukan app.siputzx.my.id!
    const val BASE_URL = "https://api.siputzx.my.id/"
    const val OPENAPI_URL = "https://api.siputzx.my.id/api/openapi.json"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.NONE })
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
