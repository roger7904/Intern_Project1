package com.example.intern_project1.model.database

import androidx.annotation.WorkerThread
import com.example.intern_project1.model.entities.Favorite
import kotlinx.coroutines.flow.Flow

class FavoriteRepository(private val favoriteDao: FavoriteDao) {

    @WorkerThread
    suspend fun insertFavoriteData(favorite: Favorite) {
        favoriteDao.insertFavorite(favorite)
    }

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allFavorite: Flow<List<Favorite>> = favoriteDao.getAllFavoriteList()


    @WorkerThread
    suspend fun deleteFavoriteData(favorite: Favorite) {
        favoriteDao.deleteFavorite(favorite)
    }

    @WorkerThread
    suspend fun deleteFavoriteDataById(id: Int) {
        favoriteDao.deleteFavoriteById(id)
    }

}