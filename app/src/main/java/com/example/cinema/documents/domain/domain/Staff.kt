package com.example.cinema.documents.domain.domain

data class Staff(
    val staffId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val description: String?,
    val posterUrl: String?,
    val professionText: String?,
    val professionKey: String?
)