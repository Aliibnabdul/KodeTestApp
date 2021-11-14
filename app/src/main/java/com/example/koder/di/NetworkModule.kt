package com.example.koder.di

import com.example.koder.BuildConfig
import com.example.koder.data.network.ApiInterface
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val CALL_TIMEOUT = 30L

@Suppress("EXPERIMENTAL_API_USAGE")
val networkModule = module {
    single {
        val json = Json {
            ignoreUnknownKeys = true
        }

        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .client(get())
            .build()
    }

    single { get<Retrofit>().create(ApiInterface::class.java) }
    single { createOkHttpClient() }
}

fun createOkHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .callTimeout(CALL_TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()
}