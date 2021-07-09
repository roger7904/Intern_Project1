package com.example.intern_project1.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.example.intern_project1.model.network.FactoryPagingSource
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import io.reactivex.rxjava3.core.Flowable


class FactoryInfoRepository(private val pagingSource: FactoryPagingSource){

    fun getMovies(): Flowable<PagingData<FactoryObject.DataX>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                maxSize = 30,
                prefetchDistance = 5,
                initialLoadSize = 40),
            pagingSourceFactory = { pagingSource }
        ).flowable
    }
}