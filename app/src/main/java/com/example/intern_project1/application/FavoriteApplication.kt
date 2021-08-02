package com.example.intern_project1.application

import android.app.Application
import com.example.intern_project1.model.database.FavoriteDatabase
import com.example.intern_project1.model.database.FavoriteRepository

class FavoriteApplication : Application() {

    private val database by lazy { FavoriteDatabase.getDatabase(this) }

    val repository by lazy { FavoriteRepository(database.favoriteDao()) }
}