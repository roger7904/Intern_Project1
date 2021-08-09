package com.example.intern_project1.model.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.intern_project1.model.entities.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Insert
    suspend fun insertFavorite(favorite: Favorite)


    @Query("SELECT * FROM favorite_table ORDER BY id")
    fun getAllFavoriteList(): Flow<List<Favorite>>

    @Query("SELECT * FROM favorite_table ORDER BY id")
    suspend fun getAllFavoriteListNotFlow(): List<Favorite>

    @Query("DELETE FROM favorite_table WHERE id = :favoriteId")
    suspend fun deleteFavoriteById(favoriteId: Int)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

}