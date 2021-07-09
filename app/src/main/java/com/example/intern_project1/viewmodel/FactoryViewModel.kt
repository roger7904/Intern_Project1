package com.example.intern_project1.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.intern_project1.model.network.FactoryApiService
import com.example.intern_project1.model.network.entities.entities.FactoryObject
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class FactoryViewModel : ViewModel() {

    private val factoryApiService = FactoryApiService()

    private val compositeDisposable = CompositeDisposable() //當頁面destroy可以自動取消訂閱，避免memory leak

    val loading = MutableLiveData<Boolean>()
    val response = MutableLiveData<FactoryObject.FactoryInfo>()
    val loadingError = MutableLiveData<Boolean>()

    fun getFactoryInfoFromApi() {
        loading.value = true

        compositeDisposable.add(
            factoryApiService.getFactoryInfo(1)
                .subscribeOn(Schedulers.newThread())//要在哪個thread執行
                .observeOn(AndroidSchedulers.mainThread())//執行後的callback要在哪個thread執行
                .subscribeWith(object : DisposableSingleObserver<FactoryObject.FactoryInfo>() {
                    override fun onSuccess(value: FactoryObject.FactoryInfo?) {
                        loading.value = false
                        response.value = value!!
                        loadingError.value = false
                    }

                    override fun onError(e: Throwable?) {
                        loading.value = false
                        loadingError.value = true
                        e!!.printStackTrace()
                    }
                })
        )
    }
}