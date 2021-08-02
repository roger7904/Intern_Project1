package com.example.intern_project1.viewmodel

import androidx.lifecycle.*
import com.example.intern_project1.model.database.FavoriteRepository
import com.example.intern_project1.model.entities.Favorite
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {

    fun insert(favorite: Favorite) = viewModelScope.launch {
        repository.insertFavoriteData(favorite)
    }

    val allFavorite: LiveData<List<Favorite>> = repository.allFavorite.asLiveData()


    fun delete(favorite: Favorite) = viewModelScope.launch {
        repository.deleteFavoriteData(favorite)
    }

    fun deleteById(id: Int) = viewModelScope.launch {
        repository.deleteFavoriteDataById(id)
    }

}


class FavoriteViewModelFactory(private val repository: FavoriteRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(
                repository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}