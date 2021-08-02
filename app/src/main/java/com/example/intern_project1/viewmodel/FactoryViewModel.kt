package com.example.intern_project1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.example.intern_project1.base.BaseState
import com.example.intern_project1.model.network.FactoryApiService
import com.example.intern_project1.model.entities.FactoryObject
import com.example.intern_project1.model.repository.FactoryInfoRepository
import com.example.intern_project1.utils.Constants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers


class FactoryViewModel(private val repository: FactoryInfoRepository) : ViewModel() {

    private val factoryApiService = FactoryApiService()

    private val compositeDisposable = CompositeDisposable() //當頁面destroy可以自動取消訂閱，避免memory leak

    val factoryInfoPagingData = MutableLiveData<PagingData<FactoryObject.DataX>>()

    val state = MutableLiveData<BaseState>()

    fun getFactoryInfoFromApi() {
        state.value = BaseState.Loading

        compositeDisposable.add(
            factoryApiService.getFactoryInfo(Constants.CONSTANT_ONE)
                .subscribeOn(Schedulers.newThread())//要在哪個thread執行
                .observeOn(AndroidSchedulers.mainThread())//執行後的callback要在哪個thread執行
                .subscribeWith(object : DisposableSingleObserver<FactoryObject.FactoryInfo>() {
                    override fun onSuccess(value: FactoryObject.FactoryInfo?) {

                        value?.let {
                            state.value = BaseState.Success(it)
                        }
                    }

                    override fun onError(e: Throwable?) {
                        e?.let {
                            state.value = BaseState.Error(it.message ?: "something went wrong")
                        }
                    }
                })
        )
    }

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

class FactoryViewModelFactory(private val repository: FactoryInfoRepository): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FactoryViewModel::class.java)) {
            return FactoryViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}