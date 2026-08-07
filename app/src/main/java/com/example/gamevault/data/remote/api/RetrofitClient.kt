package com.example.gamevault.data.remote.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Cliente Retrofit Singleton para autenticación en Twitch y consulta de la API de IGDB v4.
 */
object RetrofitClient {

    private const val BASE_URL = "https://api.igdb.com/v4/"

    /** Client ID proporcionado por la consola de desarrolladores de Twitch */
    const val TWITCH_CLIENT_ID = "x7b9sgpozax1txfj2y17z0fpbq8pbf"

    /** Client Secret proporcionado por Twitch. Generar haciendo clic en "New Secret" en la consola de Twitch */
    var TWITCH_CLIENT_SECRET = ""

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .build()

    val apiService: IgdbApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IgdbApiService::class.java)
    }
}
