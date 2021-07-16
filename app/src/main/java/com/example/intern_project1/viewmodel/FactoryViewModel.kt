package com.example.intern_project1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.rxjava3.cachedIn
import com.example.intern_project1.model.network.FactoryApiService
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import com.example.intern_project1.model.repository.FactoryInfoRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers


class FactoryViewModel(private val repository: FactoryInfoRepository) : ViewModel() {

    private val compositeDisposable = CompositeDisposable() //當頁面destroy可以自動取消訂閱，避免memory leak

    val factoryInfoPagingData = MutableLiveData<PagingData<FactoryObject.DataX>>()

    fun getFactoryInfoPagingData() {
        compositeDisposable.add(
            repository.getFactoryInfo()
                .cachedIn(viewModelScope) //緩存Flow<PagingData>
                .subscribe {
                    factoryInfoPagingData.value = it
                }
        )
    }
}