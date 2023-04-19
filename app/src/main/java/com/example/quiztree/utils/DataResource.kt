package com.example.quiztree.utils

sealed class DataResource<out T> {
    object Loading : DataResource<Nothing>()
    data class Success<T>(var data: T? = null) : DataResource<T>()
    data class Error<T>(
        val exception: Exception, val message: String, val code: Int? = null
    ): DataResource<T>()
}
