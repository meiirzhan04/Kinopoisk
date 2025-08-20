package com.example.cinema.documents.domain.repository

import com.example.cinema.documents.data.remote.FilmApi
import com.example.cinema.documents.domain.model.SearchFilm
import javax.inject.Inject

class FilmRepository @Inject constructor(
    private val api: FilmApi
) {
    suspend fun searchFilms(query: String, apiKey: String): List<SearchFilm> {
        return api.searchFilms(query, 1, apiKey).films
    }
}
