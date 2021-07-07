package com.example.intern_project1.model.network

import com.example.intern_project1.model.network.entities.entities.FactoryObject
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FactoryApi {

    @GET("api/v1/web/publicMaintenance")
    fun getFactoryInfo(
        @Query("page") page: Int,
    ): Single<FactoryObject.FactoryInfo>

}