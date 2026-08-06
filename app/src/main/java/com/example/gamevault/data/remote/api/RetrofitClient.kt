package com.example.gamevault.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit Singleton para GameVault.
 *
 * Configura:
 * - Base URL de RAWG API: https://api.rawg.io/api/
 * - OkHttp con HttpLoggingInterceptor (para ver las respuestas HTTP en Logcat)
 * - GsonConverterFactory para convertir JSON a Data Transfer Objects (DTOs)
 * - Timeouts de conexión y lectura
 */
object RetrofitClient {

    private const val BASE_URL = "https://api.rawg.io/api/"

    /**
     * API Key pública por defecto para RAWG Video Games Database.
     * Puedes reemplazarla por tu propia clave obtenida gratuitamente en https://rawg.io/apidocs
     */
    const val RAWG_API_KEY = "c53a701509054d3c965706509f635682"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val apiService: RawgApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RawgApiService::class.java)
    }
}
