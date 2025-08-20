package com.example.cinema.documents.domain.model

data class Episode(
    val seasonNumber: Int,
    val episodeNumber: Int,
    val nameRu: String?,
    val nameEn: String?,
    val synopsis: String?,
    val releaseDate: String?
)

data class Season(
    val number: Int,
    val episodes: List<Episode>
)

data class EpisodesResponse(
    val total: Int,
    val items: List<Season>
)
