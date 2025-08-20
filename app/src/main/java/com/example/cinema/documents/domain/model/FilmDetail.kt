package com.example.cinema.documents.domain.model


data class FilmDetail(
    val kinopoiskId: Int,
    val nameRu: String?,
    val posterUrl: String?,
    val posterUrlPreview: String?,
    val ratingKinopoisk: Double?,
    val genres: List<Genre>,
    val countries: List<Country>,
    val year: Int?,
    val ratingAgeLimits: String?,
    val filmLength: Int?,
    val logoUrl: String?,
    val shortDescription: String?,
    val description: String?,
    val type: String?,
    val professionKey: String?
)

data class Genre(val genre: String)
data class Country(val country: String)
