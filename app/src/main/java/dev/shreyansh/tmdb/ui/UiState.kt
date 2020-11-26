package dev.shreyansh.tmdb.ui

sealed class UiState<T>

data class Loading<T>(private val showLoader: Boolean = true) : UiState<T>()
data class Error<T>(val errorMessage: String = "") : UiState<T>()
data class Success<T>(val data: T) : UiState<T>()