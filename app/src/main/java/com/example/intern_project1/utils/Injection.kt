package com.example.intern_project1.utils

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.example.intern_project1.application.FavoriteApplication
import com.example.intern_project1.model.network.FactoryApiService
import com.example.intern_project1.model.network.FactoryPagingSource
import com.example.intern_project1.model.repository.FactoryInfoRepository
import com.example.intern_project1.viewmodel.FactoryViewModelFactory


object Injection {

    fun provideFactoryViewModel(context: Context): ViewModelProvider.Factory {
        val pagingSource =
            FactoryPagingSource(
                FactoryApiService.create(),
                (context.applicationContext as FavoriteApplication).repository
            )

        val repository =
            FactoryInfoRepository(
                pagingSource = pagingSource
            )

        return FactoryViewModelFactory(
            repository, (context.applicationContext as FavoriteApplication).repository
        )
    }

}