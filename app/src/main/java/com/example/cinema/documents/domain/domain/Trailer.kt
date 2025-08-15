package com.example.cinema.documents.domain.domain

data class TrailerResponse(
    val total: Int,
    val items: List<Trailer>
)

data class Trailer(
    val url: String,
    val name: String,
    val site: String
)
