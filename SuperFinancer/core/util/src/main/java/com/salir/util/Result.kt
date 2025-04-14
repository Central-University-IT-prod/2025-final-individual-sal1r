package com.salir.util

sealed class Result<out T> {

    data class Success<T>(val data: T, val done: Boolean = true): Result<T>()

    data object Empty: Result<Nothing>()

    data object Loading: Result<Nothing>()

    data class Failure(val message: String, val done: Boolean = true): Result<Nothing>()
}