package com.example.intern_project1.model.network

import com.example.intern_project1.model.entities.FactoryObject
import com.example.intern_project1.utils.Constants
import io.reactivex.rxjava3.core.Single
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface FactoryApiService {

    @GET(Constants.API_ENDPOINT)
    suspend fun getFactoryInfo(
        @Query(Constants.PAGE) page: Int,
    ): FactoryObject.FactoryInfo

    companion object {
        fun create(): FactoryApiService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()
            return Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(FactoryApiService::class.java)
        }
    }
}