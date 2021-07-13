package com.example.intern_project1.model.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava3.flowable
import com.example.intern_project1.model.network.FactoryPagingSource
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import io.reactivex.rxjava3.core.Flowable


class FactoryInfoRepository(private val pagingSource: FactoryPagingSource){

    fun getFactoryInfo(): Flowable<PagingData<FactoryObject.DataX>> {
        //Flowable用來解決觀察者與被觀察者傳誦與接收資料速度差距過大所會發生的記憶體不足情形
        return Pager(
            config = PagingConfig(
                initialLoadSize = 3, //初始化數據時加載的數量
                pageSize = 2, //設置每頁加載的數量
                prefetchDistance = 1, //預加載的數量
                maxSize = 10, //刪除頁面前可加載的最大item數量
                enablePlaceholders = true //當項目為空是否需要 Placeholder 默認為 true
                ),
            pagingSourceFactory = {
                pagingSource
            }
        ).flowable
    }
}