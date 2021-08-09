package com.example.intern_project1.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.rxjava3.cachedIn
import com.example.intern_project1.base.BaseState
import com.example.intern_project1.model.database.FavoriteRepository
import com.example.intern_project1.model.network.FactoryApiService
import com.example.intern_project1.model.entities.FactoryObject
import com.example.intern_project1.model.entities.Favorite
import com.example.intern_project1.model.repository.FactoryInfoRepository
import com.example.intern_project1.utils.Constants
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch


class FactoryViewModel(private val factoryRepository: FactoryInfoRepository,
                       private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val factoryApiService :FactoryApiService = FactoryApiService.create()

    private val compositeDisposable = CompositeDisposable() //當頁面destroy可以自動取消訂閱，避免memory leak

    val factoryInfoPagingData = MutableLiveData<PagingData<FactoryObject.DataX>>()

    val state = MutableLiveData<BaseState>()

    suspend fun getFactoryInfoFromApi() {
        //state.value = BaseState.Loading

        factoryApiService.getFactoryInfo(Constants.CONSTANT_ONE)?.let {
            state.value = BaseState.Success(it)
            Log.i(TAG, "getFactoryInfoFromApi: $it")
        }

    }

    fun getFactoryInfoPagingData() {
        compositeDisposable.add(
            factoryRepository.getFactoryInfo()
                .cachedIn(viewModelScope) //緩存Flow<PagingData>
                .subscribe {
                    factoryInfoPagingData.value = it
                }
        )
    }

    fun insert(favorite: Favorite) = viewModelScope.launch {
        favoriteRepository.insertFavoriteData(favorite)
    }

    val allFavorite: LiveData<List<Favorite>> = favoriteRepository.allFavorite.asLiveData()


    fun delete(favorite: Favorite) = viewModelScope.launch {
        favoriteRepository.deleteFavoriteData(favorite)
    }

    fun deleteById(id: Int) = viewModelScope.launch {
        favoriteRepository.deleteFavoriteDataById(id)
    }
}

class FactoryViewModelFactory(private val factoryRepository: FactoryInfoRepository,
                              private val favoriteRepository: FavoriteRepository
): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FactoryViewModel::class.java)) {
            return FactoryViewModel(
                factoryRepository, favoriteRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}