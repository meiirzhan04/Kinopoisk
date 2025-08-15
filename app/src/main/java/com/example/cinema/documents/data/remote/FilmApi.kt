package com.example.cinema.documents.data.remote

import com.example.cinema.documents.domain.domain.Episode
import com.example.cinema.documents.domain.domain.EpisodesResponse
import com.example.cinema.documents.domain.domain.FilmDetail
import com.example.cinema.documents.domain.domain.SearchResponse
import com.example.cinema.documents.domain.domain.Staff
import com.example.cinema.documents.domain.domain.TrailerResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmApi {
    @GET("api/v2.2/films/{id}")
    suspend fun getFilmDetail(
        @Path("id") filmId: Int,
        @Header("X-API-KEY") apiKey: String
    ): FilmDetail

    @GET("api/v2.2/films/{id}/seasons")
    suspend fun getFilmSeasons(
        @Path("id") filmId: Int,
        @Header("X-API-KEY") apiKey: String
    ): EpisodesResponse

    @GET("api/v1/staff")
    suspend fun getActors(
        @Query("filmId") filmId: Int,
        @Header("X-API-KEY") apiKey: String
    ): List<Staff>

    @GET("api/v2.1/films/search-by-keyword")
    suspend fun searchFilms(
        @Query("keyword") keyword: String,
        @Query("page") page: Int = 1,
        @Header("X-API-KEY") apiKey: String
    ): SearchResponse

    @GET("api/v2.2/films/{id}/videos")
    suspend fun getTrailer(
        @Path("id") filmId: Int,
        @Header("X-API-KEY") apiKey: String
    ): TrailerResponse
}