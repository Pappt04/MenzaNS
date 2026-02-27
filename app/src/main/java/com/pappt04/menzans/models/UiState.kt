package com.pappt04.menzans.models

sealed class UiState {
    object Empty : UiState()

    object Loading : UiState()

    data class Success(
        val data: Map<String, Double>,
    ) : UiState()

    data class Error(
        val message: String,
    ) : UiState()
}
