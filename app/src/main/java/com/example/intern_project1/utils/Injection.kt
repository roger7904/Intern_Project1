package com.example.intern_project1.utils

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.intern_project1.application.FavoriteApplication
import com.example.intern_project1.model.network.FactoryApiService
import com.example.intern_project1.model.network.FactoryPagingSource
import com.example.intern_project1.model.repository.FactoryInfoRepository
import com.example.intern_project1.viewmodel.FactoryViewModelFactory
import com.example.intern_project1.viewmodel.FavoriteViewModelFactory


object Injection {

    fun provideFactoryViewModel(context: Context): ViewModelProvider.Factory {
        val pagingSource =
            FactoryPagingSource(
                FactoryApiService(),
            )

        val repository =
            FactoryInfoRepository(
                pagingSource = pagingSource
            )

        return FactoryViewModelFactory(
            repository
        )
    }

    fun provideFavoriteViewModel(context: Context): ViewModelProvider.Factory {
        return FavoriteViewModelFactory(
            (context.applicationContext as FavoriteApplication).repository
        )
    }

}