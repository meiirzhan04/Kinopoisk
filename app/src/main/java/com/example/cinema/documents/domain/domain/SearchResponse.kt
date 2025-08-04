package com.example.cinema.documents.domain.domain

data class SearchResponse(
    val keyword: String?,
    val pagesCount: Int,
    val films: List<SearchFilm>
)

data class SearchFilm(
    val filmId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val year: String?,
    val posterUrlPreview: String?,
    val rating: String?
)