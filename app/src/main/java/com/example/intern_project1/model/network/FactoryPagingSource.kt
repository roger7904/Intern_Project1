package com.example.intern_project1.model.network


import android.util.Log
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException
import kotlin.math.log


class FactoryPagingSource(
    val factoryApiService: FactoryApiService,
    ) : RxPagingSource<Int, FactoryObject.DataX>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, FactoryObject.DataX>> {
        val position = params.key ?: 1
        Log.i("FactoryInfo Page", "$position")
        return factoryApiService
            .getFactoryInfo(position)
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, FactoryObject.DataX>> { result ->
                LoadResult.Page(
                    data = result.data.data,
                    prevKey = if (position == 1) null else position-1,
                    nextKey = if (position == result.data.last_page) null else position+1
                )
            }
            .onErrorReturn { e ->
                when (e) {
                    // Retrofit calls that return the body type throw either IOException for
                    // network failures, or HttpException for any non-2xx HTTP status codes.
                    // This code reports all errors to the UI, but you can inspect/wrap the
                    // exceptions to provide more context.
                    is IOException -> LoadResult.Error(e)
                    is HttpException -> LoadResult.Error(e)
                    else -> throw e
                }
            }
    }

    override fun getRefreshKey(state: PagingState<Int, FactoryObject.DataX>): Int? {
        TODO("Not yet implemented")
    }
}

