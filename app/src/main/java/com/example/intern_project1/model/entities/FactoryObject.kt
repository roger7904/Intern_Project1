package com.example.intern_project1.model.network.entities.entities

object FactoryObject {
    data class FactoryInfo(
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
        val weekdays: String
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