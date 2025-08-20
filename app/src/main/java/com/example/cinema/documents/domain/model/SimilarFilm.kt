package com.example.cinema.documents.domain.model

data class SimilarFilm(
    val total: Int?,
    val items: List<SimilarFilmList>
)

data class SimilarFilmList(
    val filmId: Int?,
    val nameRu: String?,
    val nameEn: String?,
    val nameOriginal: String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val relationType: String?
)
