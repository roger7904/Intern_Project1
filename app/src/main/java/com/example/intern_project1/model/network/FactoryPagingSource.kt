package com.example.intern_project1.model.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.intern_project1.model.database.FavoriteRepository
import com.example.intern_project1.model.entities.FactoryObject
import retrofit2.HttpException
import java.io.IOException

class FactoryPagingSource(
    private val service: FactoryApiService,
    private val repository: FavoriteRepository,
) : PagingSource<Int, FactoryObject.DataX>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FactoryObject.DataX> {
        val position = params.key ?: 1
        return try {
            val response = service.getFactoryInfo(position)

            val favoriteData = repository.getAllFavoriteListNotFlowData()

            for (responseItem in response.data.data.indices){
                for (favoriteItem in favoriteData) {
                    if (response.data.data[responseItem].id == favoriteItem.id){
                        response.data.data[responseItem].isFavorite = true
                        break
                    }else{
                        response.data.data[responseItem].isFavorite = false
                    }
                }
            }

            LoadResult.Page(
                data = response.data.data,
                prevKey = if (position == 1) null else position-1,
                nextKey = if (position == response.data.last_page) null else position+1
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }

    //提供頁面更新所要返回的key，如果key=null，則載入初始頁面
    override fun getRefreshKey(state: PagingState<Int, FactoryObject.DataX>): Int? {
        return state.anchorPosition
    }

}