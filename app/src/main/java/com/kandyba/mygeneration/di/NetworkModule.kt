package com.kandyba.mygeneration.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kandyba.mygeneration.data.WallApiMapper
import dagger.Module
import dagger.Provides
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    @Provides
    fun provideWallApiMapper(): WallApiMapper {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient: OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .retryOnConnectionFailure(true)
            .callTimeout(60, TimeUnit.SECONDS)
            .addNetworkInterceptor(object : Interceptor {
                override fun intercept(chain: Interceptor.Chain): Response {
                    val request = chain.request()
                        .newBuilder()
                        .addHeader("Connection", "close")
                        .build()
                    return chain.proceed(request)

                }
            })
            .build()

        val jsonBuilder = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl(VK_URL)
            .addConverterFactory(jsonBuilder.asConverterFactory("application/json".toMediaType()))
            //.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(WallApiMapper::class.java)
    }

    companion object {
        private const val VK_URL = "https://api.vk.com/method/"
    }
}