package com.example.cinema.documents.domain.model

data class Staff(
    val staffId: Int,
    val nameRu: String?,
    val nameEn: String?,
    val description: String?,
    val posterUrl: String?,
    val professionText: String?,
    val professionKey: String?
)

data class ImagesResponse(
    val total: Int,
    val totalPages: Int,
    val items: List<ImageItem>
)

data class ImageItem(
    val imageUrl: String,
    val previewUrl: String
)

