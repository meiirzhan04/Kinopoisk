package com.example.cinema.documents.presentation.screen.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinema.documents.domain.model.SearchFilm
import com.example.cinema.documents.domain.repository.FilmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: FilmRepository
) : ViewModel() {

    private val _results = MutableStateFlow<List<SearchFilm>>(emptyList())
    val results: StateFlow<List<SearchFilm>> = _results

    private val api = "f503c2e2-dcac-4045-9748-ddd23fc0bafe"

    fun searchFilms(query: String) {
        viewModelScope.launch {
            if (query.isBlank()) {
                _results.value = emptyList()
                return@launch
            }
            try {
                _results.value = repository.searchFilms(query = query, apiKey = api)
            } catch (e: Exception) {
                e.printStackTrace()
                _results.value = emptyList()
            }
        }
    }
}