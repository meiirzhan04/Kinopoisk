package com.example.cinema.documents.presentation.screen.moviedetail


sealed interface SeasonsUiState {
    object Loading : SeasonsUiState
    data class Success(val seasonsCount: Int, val episodesCount: Int) : SeasonsUiState
    data class Error(val message: String) : SeasonsUiState
}
