package com.pappt04.menzans.service

import com.pappt04.menzans.BuildConfig
import com.pappt04.menzans.data.consts.AppConfig.APP_API_KEY
import com.pappt04.menzans.data.consts.AppConfig.BASE_SERVER_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val okHttpClient =
        OkHttpClient
            .Builder()
            .apply {
                // Attach the API key to every request
                addInterceptor { chain ->
                    val request = chain.request().newBuilder()
                        .header("X-API-Key", APP_API_KEY)
                        .build()
                    chain.proceed(request)
                }
                if (BuildConfig.DEBUG) {
                    addInterceptor(
                        HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        }
                    )
                }
            }
            .build()

    private val retrofit: Retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_SERVER_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: MenzaApiService by lazy {
        retrofit.create(MenzaApiService::class.java)
    }
}
