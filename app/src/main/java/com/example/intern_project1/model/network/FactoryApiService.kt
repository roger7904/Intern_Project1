package com.example.intern_project1.model.network

import com.example.intern_project1.model.network.entities.entities.FactoryObject
import io.reactivex.rxjava3.core.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class FactoryApiService {

    private val api = Retrofit.Builder()
        .baseUrl("https://kagulubs.25demo.com/")
        .addConverterFactory(GsonConverterFactory.create()) //Gson to Json
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create()) //呼叫Rxjava3的adapter，用來回傳observable, single
        .build()
        .create(FactoryApi::class.java)

    fun getFactoryInfo(): Single<FactoryObject.FactoryInfo> {
        return api.getFactoryInfo(
            1
        )
    }
}