package com.example.intern_project1.model.network


import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException


class FactoryPagingSource(
    val factoryApiService: FactoryApiService,
    val page: Int
    ) : RxPagingSource<Int, FactoryObject.DataX>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, FactoryObject.DataX>> {
        return factoryApiService
            .getFactoryInfo(page)
            .subscribeOn(Schedulers.io())
            .map<LoadResult<Int, FactoryObject.DataX>> { result ->
                LoadResult.Page(
                    data = result.data.data,
                    prevKey = if (result.data.current_page == 1) null else result.data.current_page-1,
                    nextKey = result.data.current_page+1
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

