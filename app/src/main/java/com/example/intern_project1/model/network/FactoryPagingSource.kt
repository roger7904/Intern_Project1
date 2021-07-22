package com.example.intern_project1.model.network


import android.util.Log
import androidx.paging.PagingState
import androidx.paging.rxjava3.RxPagingSource
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import retrofit2.HttpException
import java.io.IOException



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
                    is IOException -> LoadResult.Error(e)
                    is HttpException -> LoadResult.Error(e)
                    else -> throw e
                }
            }
    }

    //提供頁面更新所要返回的key，如果key=null，則載入初始頁面
    override fun getRefreshKey(state: PagingState<Int, FactoryObject.DataX>): Int? {
        return state.anchorPosition
    }
}

