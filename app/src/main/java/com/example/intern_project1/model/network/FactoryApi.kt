package com.example.intern_project1.model.network

import com.example.intern_project1.model.network.entities.entities.FactoryObject
import com.example.intern_project1.utils.Constants
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface FactoryApi {

    @GET(Constants.API_ENDPOINT)
    fun getFactoryInfo(
        @Query(Constants.PAGE) page: Int,
    ): Single<FactoryObject.FactoryInfo> //通常 api request 只會需要請求後回傳成功或失敗就好，不用on next等等callback

}