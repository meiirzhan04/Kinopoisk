package com.example.cinema.documents.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.cinema.documents.domain.model.FilmDetail
import com.example.cinema.documents.presentation.components.movie.MovieCard
import com.example.cinema.documents.presentation.components.movie.MovieCardShimmer


@Composable
fun MovieListOrShimmer(films: List<FilmDetail>, onItemClick: (FilmDetail) -> Unit) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item { Spacer(modifier = Modifier.width(16.dp)) }

        if (films.isEmpty()) {
            items(6) { MovieCardShimmer() }
        } else {
            items(films) { film ->
                MovieCard(
                    title = film.nameRu ?: "Без названия",
                    genre = film.genres.firstOrNull()?.genre ?: "Жанр не указан",
                    rating = film.ratingKinopoisk ?: 0.0,
                    posterUrl = film.posterUrlPreview ?: "",
                    onClick = { onItemClick(film) }
                )
            }
        }
    }
}