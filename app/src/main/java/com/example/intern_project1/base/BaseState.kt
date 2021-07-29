package com.example.intern_project1.base

import com.example.intern_project1.model.entities.FactoryObject

sealed class BaseState {
    data class Success(val data: FactoryObject.FactoryInfo) : BaseState()
    data class Error(val message: String) : BaseState()
    object Loading : BaseState()
}