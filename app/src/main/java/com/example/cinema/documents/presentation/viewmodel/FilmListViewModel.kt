package com.example.cinema.documents.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinema.documents.data.remote.FilmApi
import com.example.cinema.documents.domain.model.FilmDetail
import com.example.cinema.documents.domain.model.Trailer
import com.example.cinema.documents.presentation.screen.moviedetail.SeasonsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


sealed interface FilmListUiState {
    object Loading : FilmListUiState
    data class Success(val films: List<FilmDetail>) : FilmListUiState
    data class Error(val message: String) : FilmListUiState
}

@HiltViewModel
class FilmListViewModel @Inject constructor(
    private val api: FilmApi
) : ViewModel() {

    private val _uiState = MutableStateFlow<FilmListUiState>(FilmListUiState.Loading)
    val uiState: StateFlow<FilmListUiState> = _uiState

    fun loadFilms(ids: List<Int>, apiKey: String) {
        if (_uiState.value is FilmListUiState.Success) return

        viewModelScope.launch {
            try {
                _uiState.value = FilmListUiState.Loading
                val result = ids.map { id ->
                    async { api.getFilmDetail(id, apiKey) }
                }.awaitAll()
                _uiState.value = FilmListUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = FilmListUiState.Error(e.message ?: "Unknown error")
            }
        }
    }


    private val _film = MutableStateFlow<FilmDetail?>(null)
    val film: StateFlow<FilmDetail?> = _film

    private val _trailers = MutableStateFlow<Trailer?>(null)
    val trailer: StateFlow<Trailer?> = _trailers

    private val _seasonsState = MutableStateFlow<SeasonsUiState>(SeasonsUiState.Loading)
    val seasonsState: StateFlow<SeasonsUiState> = _seasonsState
    fun loadFilm(id: Int, apiKey: String) {
        viewModelScope.launch {
            try {
                _film.value = api.getFilmDetail(id, apiKey)
            } catch (e: Exception) {
                e.printStackTrace()
                _film.value = null
            }
        }
    }


    fun loadSeasonsAndEpisodes(filmId: Int, apiKey: String) {
        viewModelScope.launch {
            try {
                _seasonsState.value = SeasonsUiState.Loading
                val response = api.getFilmSeasons(filmId, apiKey)

                val seasonsCount = response.total
                val episodesCount = response.items.sumOf { it.episodes.size }

                _seasonsState.value = SeasonsUiState.Success(seasonsCount, episodesCount)
            } catch (e: Exception) {
                _seasonsState.value = SeasonsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getTrailer(filmId: Int) {
        viewModelScope.launch {
            try {
                val response = api.getTrailer(filmId = filmId, "fdbc3912-2979-460b-afeb-70590710993a")
                val trailerUrl = response.items.firstOrNull { it.site == "YOUTUBE" }?.url
                Log.d("Trailer", "URL: $trailerUrl")
            } catch (e: Exception) {
                Log.e("API_ERROR", "Error loading trailer", e)
            }
        }
    }
}