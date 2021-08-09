package com.example.intern_project1.model.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

object FactoryObject {

    data class FactoryInfo(
        val data: Data,
        val message: String,
        val status: Int
    )

    data class Data(
        val current_page: Int,
        val data: List<DataX>,
        val first_page_url: String,
        val from: Int,
        val last_page: Int,
        val last_page_url: String,
        val next_page_url: String,
        val path: String,
        val per_page: Int,
        val prev_page_url: Any,
        val to: Int,
        val total: Int
    )

    data class DataX(
        val address: String,
        val city: String,
        val code: String,
        val contact_person: String,
        val created_at: String,
        val distance: Any,
        val district: String,
        val email: String,
        val flag: Int,
        val id: Int,
        val introduction: String,
        val latitude: String,
        val longitude: String,
        val maintain: Int,
        val maintenance_plant_photo: List<MaintenancePlantPhoto>,
        val mobile: String,
        val mobile_2: String,
        val name: String,
        val open_time: String,
        val open_time_from: Any,
        val open_time_to: Any,
        val order: Int,
        val password: String,
        val phone: String,
        val phone_2: String,
        val series: String,
        val source: Any,
        val status: String,
        val tags: String,
        val type: String,
        val updated_at: String,
        val watch: Int,
        val weekdays: String,
        var isFavorite: Boolean?,
    )

    data class MaintenancePlantPhoto(
        val cover: String,
        val created_at: String,
        val filename: String,
        val id: Int,
        val maintenance_plant_id: Int,
        val order: Int,
        val updated_at: String
    )
}

@Entity(tableName = "favorite_table")
data class Favorite(
    @ColumnInfo val name: String,
    @ColumnInfo val address: String,
    @ColumnInfo val city: String,
    @ColumnInfo val phone: String,
    @ColumnInfo val tags: String,
    // Specifies the name of the column in the table if you want it to be different from the name of the member variable.
    @ColumnInfo val maintenance_plant_photo: String,
    @PrimaryKey val id: Int,
)